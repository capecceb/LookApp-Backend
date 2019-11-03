package lookapp.backend
import grails.rest.Resource

@Resource(uri='/workingHours')
class WorkingHour {

    Day days
    Integer beginHour
    Integer endHour
    static constraints = {
    }
}
