package lookapp.backend


class Appointment {

    Date dayHour
    Date endDate
    String local
    Client client
    Professional professional
    Branch branch
    String eventId
    Date dateCreated
    Date lastUpdated
    BigDecimal totalPrice
    BigDecimal totalToPay

    AppointmentStatus status
    static hasMany = [services: Service, payments: Payment]
    static constraints = {
        client(nullable: true)
        professional(nullable: true)
        endDate(nullable: true)
        dayHour(column: "begin_date")
        eventId(nullable: true)
    }
}
