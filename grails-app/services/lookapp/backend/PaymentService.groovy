package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Long appointmentId, BigDecimal amount, String currency, Long clientId, Integer points) {

        BigDecimal amountEntered = 0;
        BigDecimal totalCost = 0;
        BigDecimal totalAmount = 0;

        Client client = null
        Appointment appointment = Appointment.get(appointmentId)
        if (appointment == null) {
            throw new BadRequestException("Invalid appointment id")
        }
        if (clientId != null) {
            client = Client.get(clientId)
        }
        if (amount != null) {
            amountEntered = amount
        }
        for (Service service in appointment.services) {
            //verify if there are a promotions with discounts
            Float discount = verifyDiscounts(service)
            totalCost += service.price * discount
        }
        totalCost=totalCost.setScale(2,BigDecimal.ROUND_DOWN)
        if (appointment.payments == null) {
            appointment.payments = new ArrayList<>()
        }
        for (Payment pay in appointment.payments) {
            totalAmount += pay.amount
        }
        BigDecimal amountFromPoints = 0;
        if (points != null && client != null) {
            if (client.points >= points) {
                //verify if there are a promotions with points
                Float pointFactor = verifyPointFactor(appointment.services)
                amountFromPoints = points / Integer.parseInt(Config.findByKey("changePurchase").value) * pointFactor
                client.points -= points
            } else {
                throw new BadRequestException("The amount of client points is insufficient")
            }
        } else if (points != null && client == null) {
            throw new BadRequestException("Error: Can't make a payment with points without a client")
        }
        if (amountEntered != 0 && client != null) {
            Long newPoints = amountEntered.toInteger() * Integer.parseInt(Config.findByKey("changePay").value)
            client.points += newPoints
        }
        if(client!=null)  client.save()
        totalAmount = amountEntered + amountFromPoints
        if (totalCost > totalAmount) {
            appointment.status = AppointmentStatus.PARTIAL_PAID
        }
        if (totalCost == totalAmount) {
            appointment.status = AppointmentStatus.PAID
        }
        if (totalCost < totalAmount) {
            throw new BadRequestException("Error payment exceeds cost")
        }

        Payment payment = new Payment()
        payment.appointment = appointment
        payment.amount = amountEntered
        payment.currency = currency
        payment.save()

        appointment.payments.add(payment)
        appointment.save()

        AccountMovement accountMovement = new AccountMovement()
        accountMovement.amount = totalAmount
        accountMovement.save(flush: true)

        client.accountancy.accountMovements.add(accountMovement)
        client.save(flush: true)
        return appointment
    }

    private Float verifyDiscounts(Service service){
        Float discount=100
        Date now=new Date()
        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", now)
            gte("endDate", now)
            eq("status",PromotionStatus.ACTIVE)
            eq("type",PromotionType.DISCOUNT)
            services{
                eq("id",service.id)
            }
        }
        for (Promotion promotion : promotions) {
            discount=discount-promotion.discount
        }
        if(discount<0) discount=0
        return discount/100
    }

    private Float verifyPointFactor(Set<Service> servicesToVerify){
        Float pointFactor=1
        Date now=new Date()
        def ids=servicesToVerify.collect{element -> return element.id}
        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", now)
            gte("endDate", now)
            eq("status",PromotionStatus.ACTIVE)
            eq("type",PromotionType.POINT)
            services{
                inList("id",ids)
            }
        }
        for (Promotion promotion : promotions) {
            pointFactor=pointFactor*promotion.pointFactor
        }
        return pointFactor
    }
}
