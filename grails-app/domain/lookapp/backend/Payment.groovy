package lookapp.backend


class Payment {

    BigDecimal amount
    String currency
    Appointment appointment

    Date dateCreated
    Date lastUpdated

    static constraints = {
    }

}
