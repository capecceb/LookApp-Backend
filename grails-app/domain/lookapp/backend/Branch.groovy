package lookapp.backend

import grails.rest.Resource


@Resource(uri='/branches')
class Branch {
    String name;
    String address;
    
    BranchStatus status

    Date dateCreated
    Date lastUpdated

    static hasMany = [professionals : Professional , users : User]

    static constraints = {
        professionals(nullable:true)
        users(nullable:true)
    }
}