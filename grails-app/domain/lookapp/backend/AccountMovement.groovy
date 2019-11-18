package lookapp.backend

class AccountMovement {

    Appointment appointment
    BigDecimal amount
    Date dateCreated

    static constraints = {
        appointment(nullable: true)
    }
}
