package lookapp.backend
import grails.rest.Resource

@Resource(uri='/appointments')
class Appointment {

    Date dayHour
    String local
    Client client

    Date dateCreated
    Date lastUpdated

    AppointmentStatus status
    static hasMany = [services: Service]
    static constraints = {
        client(nullable:true)
    }

    def beforeInsert() {
        status=AppointmentStatus.OPEN
    }
}
