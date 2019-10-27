package lookapp.backend


class Appointment {

    Date dayHour
    Date endDate
    String local
    Client client
    Professional professional

    Date dateCreated
    Date lastUpdated
    AppointmentStatus status
    static hasMany = [services: Service]
    static hasManyPayments = [payments: Payment]
    static constraints = {
        client(nullable:true)
        professional(nullable:true)
        endDate(nullable:true)
        dayHour(column:"begin_date")
    }

    def beforeInsert() {
        status=AppointmentStatus.OPEN
    }
    def beforeUpdate() {
    }
}
