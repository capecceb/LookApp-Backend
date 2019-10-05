package lookapp.backend
import grails.rest.Resource

@Resource(uri='/appointments')
class Appointment {

    Date dayHour
    String local

    Date dateCreated
    Date lastUpdated

    AppointmentStatus appointmentStatus
    static hasMany = [services: Service]
    static constraints = {
    }
}
