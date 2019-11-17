package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class PaymentService {
    def save(Long appointmentId, BigDecimal amount, String currency, Long clientId, Integer points) {

        BigDecimal amountEntered = 0;
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
                client == null ? println("client es null") : println("client no es null")
                appointment.dayHour == null ? println("appointmenr day hour es null") : println("appointmenr day hour no es null")
                appointment.services == null ? println("appointmenr services es null") : println("appointmenr services no es null")
                Float pointFactor = verifyPointFactor(appointment.dayHour,appointment.services)
                println("Paso verify point factor")
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

        if (appointment.totalToPay > totalAmount) {
            appointment.status = AppointmentStatus.PARTIAL_PAID
        }
        if (appointment.totalToPay == totalAmount) {
            appointment.status = AppointmentStatus.PAID
        }
        if (appointment.totalToPay < totalAmount) {
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
        accountMovement.appointment = appointment

        if(appointment.status == AppointmentStatus.PAID){
            accountMovement.amount = appointment.totalToPay
            client.accountancy.accountMovements.add(accountMovement)
            println("total paid")
        }
        else if(appointment.status == AppointmentStatus.PARTIAL_PAID){
            if(client.accountancy.accountMovements.size() < 1){

                accountMovement.amount = -(appointment.totalToPay)
                accountMovement.save()
                client.accountancy.accountMovements.add(accountMovement)
                println("Registrado costo total : " + appointment.totalToPay)

                AccountMovement movementPartialPaid = new AccountMovement()
                movementPartialPaid.appointment = appointment
                movementPartialPaid.amount = totalAmount
                movementPartialPaid.save()
                client.accountancy.accountMovements.add(movementPartialPaid)
                println("Registrado monton pagado: " + totalAmount)

                println("partial paid with no movements, amount entered: " + totalAmount )
            }
            else{
                accountMovement.amount = totalAmount
                accountMovement.save()

                client.accountancy.accountMovements.add(accountMovement)

                println("partial paid with movements, amount: " + totalAmount)
            }
        }

        client.save()
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

    private Float verifyPointFactor(Date beginDate,Set<Service> servicesToVerify){
        Float pointFactor = 1
        println("Entro al verify point factor")
        def ids = servicesToVerify.collect{element -> return element.id}
        println("paso service to verify")
        def promotionCriteria = Promotion.createCriteria()
        println("paso prmotion.create Criteria")
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", beginDate)
            println("start date: " + beginDate)
            gte("endDate", beginDate)
            println("end Date: " + beginDate)
            eq("status", PromotionStatus.ACTIVE)
            println("status: " + PromotionStatus.ACTIVE)
            eq("type", PromotionType.POINT)
            println("type: " + PromotionType.POINT)
            services{
                inList("id",ids)
                println("services ids")
            }
            println("salio de services")
        } as List<Promotion>
        println("paso list<promotion>")
        for (Promotion promotion : promotions) {
            println("entro al for de promotions")
            Float proFactor = promotion.pointFactor
            println("aisgno profactor")
            pointFactor = (pointFactor as Float) * (proFactor as Float)
            println("point factor * pormotion point factor: " + pointFactor)
        }
        return pointFactor
    }
}
