package lookapp.backend

import grails.rest.Resource

@Resource(uri='/roles')
class Rol {
    String name;
    String description;

    Date dateCreated
    Date lastUpdated
    static constraints = {
        description(nullable:true)
    }
}
