package lookapp.backend

import grails.rest.Resource


@Resource(uri='/branches')
class Branch {
    String name;
    String address;
    
    BranchStatus status

    User user 

    Date dateCreated
    Date lastUpdated

    static hasMany = [professionals : Professional]

    static constraints = {
        professionals(nullable:true)
        user(nullable:true)
    }
}