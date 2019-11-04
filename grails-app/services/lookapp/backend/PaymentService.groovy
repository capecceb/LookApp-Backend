package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Integer appointmentId, BigDecimal amount, String currency, Integer clientId, Integer points) {

        BigDecimal amountEntered = 0;
        BigDecimal totalCost = 0;
        BigDecimal totalAmount = 0;
        int newPoints = 0;
        def services = new ArrayList<>()
        Client client = null

        Appointment appointment = Appointment.get(appointmentId)
        if (appointment == null) {
            throw new BadRequestException("Invalid appointment id")
        }
        if(clientId != null) {
            client =  Client.get(clientId)
        }
        if(amount != null)
        {
            amountEntered = amount
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
        BigDecimal amountFromPoints = 0;
        if (points != null && client != null) {
            if(client.points >= points) {
                amountFromPoints = points / Integer.parseInt(Config.findByKey("changePurchase").value)
                client.points -= points
            }else{
                throw new BadRequestException("The amount of client points is insufficient")
            }
        }else if(points != null && client == null)
        {
            throw new BadRequestException("Error: Can't make a payment with points without a client")
        }
        if (amountEntered != 0 && client != null) {
            newPoints = amountEntered.toInteger() * Integer.parseInt(Config.findByKey("changePay").value)
            client.points += newPoints
            client.save()
        }
        amountEntered += amountFromPoints
        totalAmount += amountEntered
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
        payment.amount = amountEntered
        payment.currency = currency
        payment.save()

        appointment.payments.add(payment)
        appointment.save()

        return appointment
    }
}
