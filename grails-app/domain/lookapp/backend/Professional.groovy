package lookapp.backend

import grails.rest.Resource

@Resource(uri='/professionals')
class Professional {
    String name
    String lastName
    String phone
    String email
    ProfessionalStatus status
    Branch branch
    Date dateCreated
    Date lastUpdated

    static hasMany = [services: Service, workingHours:WorkingHour ]
    static constraints = {
        lastName(nullable:true)
        phone(nullable:true)
        email(nullable:true)
        workingHours(nullable:true)
        branch(nullable:true)
    }
}

