package lookapp.backend
import grails.rest.Resource

@Resource(uri='/workingHours')
class WorkingHour {

    Day days
    int beginHour
    int endHour
    static constraints = {
    }
}
