package lookapp.backend

import grails.rest.Resource


@Resource(uri='/branches')
class Branch {
    String name;
    String address;
    
    BranchStatus status

    // Appointment Appointment ** Branch - Appointment relation is for later

    User user 

    Date dateCreated
    Date lastUpdated

    static hasMany = [professionals : Professional, turnos : Appointment]

    static constraints = {


    }
}