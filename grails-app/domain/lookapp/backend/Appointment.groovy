package lookapp.backend
import grails.rest.Resource

@Resource(uri='/appointments')
class Appointment {

    Date dayHour
    String local
    Client client
    Professional professional

    Date dateCreated
    Date lastUpdated
    AppointmentStatus status
    static hasMany = [services: Service]
    static constraints = {
        client(nullable:true)
        professional(nullable:true)
    }

    def beforeInsert() {
        status=AppointmentStatus.OPEN
    }
}
