package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Payment payment, Integer appointmentId, BigDecimal amount, Integer clientId, int points) {

        BigDecimal totalCost = 0;
        BigDecimal parcialAmount = 0;
        int newPoints = 0;
        def services = new ArrayList<>()

        Appointment appointment = Appointment.get(appointmentId)
        if (appointment == null) {
            throw new BadRequestException("Invalid appointment id")
        }
        payment.appointment = appointment

        Client client = Client.get(clientId)
        if (client == null) {
            throw new BadRequestException("Invalid client id")
        }
        services = appointment.services
        for (Service service in services) {
            totalCost += service.price
        }
        if (appointment.payments == null){
            appointment.payments = new ArrayList<>()
        }
        for( Payment pay in appointment.payments){
            parcialAmount += pay.amount
        }
        payment.amount = amount
        appointment.payments.add(payment)
        if(amount !=0){
            newPoints = amount * Point.changePay
        }
        if(points !=0){
            points = points/Point.changePurchase
        }

        amount += parcialAmount + points
        if(totalCost > amount){
            appointment.status.PARCIALPAID
        }
        if(totalCost == amount){
            appointment.status.PAID
        }
        if(amount == 0){
            appointment.status.PENDINGPAID
        }
        appointment.save()
        client.save()
        payment.save()
        return payment
    }
}
