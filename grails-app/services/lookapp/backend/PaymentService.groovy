package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Integer appointmentId, BigDecimal amount, String currency, Integer clientId, Integer points) {

        BigDecimal totalCost = 0;
        BigDecimal totalAmount = 0;
        int newPoints = 0;
        def services = new ArrayList<>()

        Appointment appointment = Appointment.get(appointmentId)
        if (appointment == null) {
            throw new BadRequestException("Invalid appointment id")
        }
        Client client = Client.get(clientId)
        if (client == null) {
            throw new BadRequestException("Invalid client id")
        }
        services = appointment.services
        for (Service service in services) {
            totalCost += service.price
        }
        if (appointment.payments == null) {
            appointment.payments = new ArrayList<>()
        }
        for (Payment pay in appointment.payments) {
            totalAmount += pay.amount
        }
        if (amount != 0) {
            newPoints = amount.toInteger() * Integer.parseInt(Config.findByKey("changePay").value)
            client.points += newPoints
        }
        BigDecimal amountFromPoints
        if (points != null) {
            amountFromPoints = points / Integer.parseInt(Config.findByKey("changePurchase").value)
            totalAmount += amountFromPoints
            client.points -= points
        }
        if (totalCost > totalAmount) {
            appointment.status=AppointmentStatus.PARTIAL_PAID
        }
        if (totalCost == totalAmount) {
            appointment.status=AppointmentStatus.PAID
        }
        if (totalCost < totalAmount) {
            throw new BadRequestException("Error payment exceeds cost")
        }

        Payment payment = new Payment()
        payment.appointment=appointment
        payment.amount = amount
        payment.currency = currency
        payment.save()

        appointment.payments.add(payment)
        appointment.save()

        client.save()
        return appointment
    }
}
