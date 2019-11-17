package lookapp.backend

import grails.rest.Resource

class Promotion {

    String name
    PromotionStatus status
    PromotionType type
    Long discount
    Long pointFactor
    Date startDate
    Date endDate
    static hasMany = [services: Service]

    Date dateCreated
    Date lastUpdated

    static constraints = {
        discount(nullable: true)
        pointFactor(nullable: true)
    }
}
