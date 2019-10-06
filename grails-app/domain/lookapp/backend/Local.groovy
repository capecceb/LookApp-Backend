package lookapp.backend

import grails.rest.Resource

import javax.transaction.Status

@Resource(uri='/roles')
class Local {
    String name;
    String address;

    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
