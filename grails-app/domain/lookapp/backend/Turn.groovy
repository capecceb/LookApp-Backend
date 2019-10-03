package lookapp.backend
import grails.rest.Resource

@Resource(uri='/turns')
class Turn {

    Date dayHour
    String local
    Service service

    Date dateCreated
    Date lastUpdated
    static constraints = {
    }
}
