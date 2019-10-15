package lookapp.backend

import grails.rest.Resource

import javax.transaction.Status

@Resource(uri='/branchs')
class Branch {
    String name
    String address
    ProfessionalStatus status

    Date dateCreated
    Date lastUpdated

    static constraints = {
    }
}
