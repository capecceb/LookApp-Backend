package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Payment payment, Integer appointmentId, BigDecimal amount, String currency, Integer clientId, Integer points) {

        BigDecimal totalCost = 0;
        BigDecimal parcialAmount = 0;
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
            parcialAmount += pay.amount
        }
        payment.appointment = appointment
        payment.amount = amount
        payment.currency = currency
        appointment.payments.add(payment)
        if (amount != 0) {
            newPoints = amount * Point.changePay
            client.points += newPoints
        }
        BigDecimal amountFromPoints
        if (points != 0) {
            amountFromPoints = points / Point.changePurchase
            client.points -= points
        }
        amount += parcialAmount + amountFromPoints
        if (totalCost > amount) {
            appointment.status.PARTIAL_PAID
        }
        if (totalCost == amount) {
            appointment.status.PAID
        }
        if(totalCost < amount) {
            throw new BadRequestException("Error")
        }
        appointment.save()
        client.save()
        payment.save()
        return payment
    }
}
