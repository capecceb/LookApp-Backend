package lookapp.backend

import grails.rest.Resource

@Resource(uri = '/accountancy')
class Accountancy {

    Date dateCreated
    Date lastUpdated

    static hasMany = [accountMovement : AccountMovement]

    static constraints = {

    }

}
