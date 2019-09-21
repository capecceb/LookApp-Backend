package lookapp.backend
import grails.rest.*

@Resource(uri='/test')
class Test {

    String name;
    String description;

    static constraints = {
        description(nullable:false)
    }
}
