package lookapp.backend
import grails.rest.Resource

@Resource(uri='/services')
class Service {

    String name
    Integer duration
    BigDecimal price

    Date dateCreated
    Date lastUpdated
    static constraints = {
    }
}
