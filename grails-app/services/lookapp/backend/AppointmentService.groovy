package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class AppointmentService {

    def save(Appointment appointment,String local,Date beginDate,List<Service> services,
             Integer clientId,Integer professionalId){
        int duration
        for (Integer serviceId : services) {
            Service service = Service.get(serviceId)
            if (service == null) {
                throw new BadRequestException("Invalid service id")
            }
            duration = service.duration
            if(appointment.services==null) {
                appointment.services=new ArrayList<>()
            }
            appointment.services.add(service)
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate)
        calendar.add(Calendar.MINUTE, duration)
        Date endDate = calendar.getTime()
        appointment.dayHour = beginDate
        appointment.endDate = endDate
        appointment.local = local
        if (clientId != null) {
            Client client = Client.get(clientId)
            if (client == null) {
                throw new BadRequestException("Invalid client id")
            }
            appointment.client = client
        }
        if (professionalId != null) {
            Professional professional = Professional.get(professionalId)
            if (professional == null) {
                throw new BadRequestException("Invalid professional id")
            }
            def criteria = Appointment.createCriteria()
            List<Appointment> appointmentList = criteria.list {
                lt("dayHour", endDate)
                gt("endDate", beginDate)
                eq("professional", professional)
                if(appointment.id!=null) {
                    ne("id", appointment.id)
                }
            }
            if (appointmentList.size() > 0) {
                throw new BadRequestException("professional is busy")
            }
            appointment.professional = professional
        } else {
            List<Professional> professionals = availableProfessionals(appointment.id,beginDate, endDate)
            if (professionals.size() == 0) {
                throw new BadRequestException("there are no free professionals")
            }
        }
        appointment.status = AppointmentStatus.OPEN
        appointment.save()
        return appointment
    }

    def availableProfessionals(Long appointmentId,Date beginDate,Date endDate){
        def res=[:]

        Calendar calendarBegin=Calendar.getInstance()
        calendarBegin.setTime(beginDate)
        int dayOfWeek = calendarBegin.get(Calendar.DAY_OF_WEEK);
        Day day=Day.byId(dayOfWeek)

        Calendar calendarEnd=Calendar.getInstance()
        calendarEnd.setTime(endDate)
        //looking for professional that working this day
        def professionCriteria=Professional.createCriteria()
        List<Professional> professionals=professionCriteria.list{
            workingHours{
                eq("days",day)
                lte("beginHour",calendarBegin.get(Calendar.HOUR_OF_DAY))
                gte("endHour",calendarEnd.get(Calendar.HOUR_OF_DAY))
            }
        }

        def appointmentCriteria=Appointment.createCriteria()
        List<Appointment> appointmentList=appointmentCriteria.list{
            lte("dayHour", endDate)
            gt("endDate",beginDate)
            if(appointmentId!=null) {
                ne("id", appointmentId)
            }
        }
        List<Professional> resultProfessionals=new ArrayList<>()
        if(professionals.size()<=appointmentList.size()){
            return resultProfessionals
        }

        for(Professional professional:professionals){
            boolean found=false
            for(Appointment appointment:appointmentList){
                if(appointment.professional!=null && professional.id==appointment.professional.id) found=true
                break
            }
            if(!found) resultProfessionals.add(professional)
        }
        return resultProfessionals
    }
}
