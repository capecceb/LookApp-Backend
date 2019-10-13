package lookapp.backend
import grails.rest.Resource

@Resource(uri='/clients')
class Client {

    String name
    String lastName
    String DNI
    int primaryPhone
    int secondPhone
    ClientStatus status

    Date dateCreated
    Date lastUpdated

    static constraints = {
        secondPhone(nullable:true)
    }
}
