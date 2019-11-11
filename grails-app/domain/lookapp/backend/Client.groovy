package lookapp.backend
import grails.rest.Resource

@Resource(uri='/clients')
class Client {

    String name
    String lastName
    String DNI
    String email
    String primaryPhone
    String secondPhone
    Integer points
    ClientStatus status
    Accountancy accountancy

    Date dateCreated
    Date lastUpdated

    static constraints = {
        primaryPhone(nullable:true)
        secondPhone(nullable:true)
        points(nullable:true)
    }
}
