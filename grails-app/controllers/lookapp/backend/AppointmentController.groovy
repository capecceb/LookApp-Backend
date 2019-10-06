package lookapp.backend


import grails.rest.*
import grails.converters.*

class AppointmentController extends RestfulController {
    static responseFormats = ['json', 'xml']
    AppointmentController() {
        super(Appointment)
    }

    def cancel(){
        def params=getParams()
        Appointment appointment=Appointment.get(params.id)
        if(appointment==null){
            respond("Not Found",status: 404)
        }
        if(appointment.status!=AppointmentStatus.OPEN){
            respond("Wrong Status",status: 400)
        }
        appointment.status=AppointmentStatus.CANCELED
        appointment.save()
        respond(appointment,status: 200)
    }
}
