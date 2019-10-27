package lookapp.backend
import grails.rest.Resource

@Resource(uri='/clients')
class Client {

    String name
    String lastName
    String DNI
    String email
    int primaryPhone
    int secondPhone
    int point
    ClientStatus status

    Date dateCreated
    Date lastUpdated

    static constraints = {
        primaryPhone(nullable:true)
        secondPhone(nullable:true)
        point(nulleble:true)
    }
}
