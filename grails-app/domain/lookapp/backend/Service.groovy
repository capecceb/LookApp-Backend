package lookapp.backend
import grails.rest.Resource

@Resource(uri='/services')
class Service {

    String name
    int duration
    BigDecimal cost
    static constraints = {
    }
}
