package lookapp.backend

class AppointmentStatusController {
	static responseFormats = ['json', 'xml']

    def open(){
        def res=[:]
        def params=request.getJSON()

        if(params.services==null || params.services.size==0){
            res["message"]="Services cant be null"
            respond(res,status: 400)
        }
        int duration
        for(Service service:params.services){
            Service service1=Service.get(service.id)
            if(service1==null){
                res["message"]="Invalid service id"
                respond(res,status: 400)
            }
            duration=service1.duration
        }
        if(params.dayHour==null){
            res["message"]="dayHour cant be null"
        }
        Date beginDate = params.dayHour
        Date endDate = params.dayHour
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(endDate)
        calendar.add(Calendar.MINUTE,params.duration)
        endDate=calendar.getTime()
        def criteria=Appointment.createCriteria()
        List<Appointment> appointmentList = criteria.list{
            lt("dayHour", beginDate)
            gt("dayHour",endDate)
            if(params.professional.id){
                professional{
                    eq("id",params.professional.id)
                }
            }
        }
    }

    def cancel() {
        def res=[:]
        def params=getParams()
        Appointment appointment=Appointment.get(params.id)
        if(appointment==null){
            res["message"]="Not Found"
            respond(res,status: 404)
            return
        }
        if(appointment.status!=AppointmentStatus.OPEN){
            res["message"]="Invalid Status"
            respond(res,status: 400)
            return
        }
        appointment.status=AppointmentStatus.CANCELED
        Appointment.withNewTransaction {
            appointment.save()
        }
        respond(appointment,status: 200)
    }
    def paid(){
        def res=[:]
        def params=getParams()
        Appointment appointment=Appointment.get(params.id)
        if(appointment==null){
            res["message"]="Not Found"
            respond(res,status: 404)
        }
        if(appointment.status!=AppointmentStatus.OPEN){
            res["message"]="Invalid Status"
            respond(res,status: 400)
        }
        appointment.status=AppointmentStatus.PAID
        Appointment.withNewTransaction {
            appointment.save()
        }
        respond(appointment,status: 200)
    }

}
