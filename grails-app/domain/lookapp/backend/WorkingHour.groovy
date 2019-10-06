package lookapp.backend
import grails.rest.Resource

@Resource(uri='/workingHours')
class WorkingHour {

    Day days
    String beginHour
    String endHour
    static constraints = {
    }
}
