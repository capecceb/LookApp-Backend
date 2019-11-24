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

        def paymentHistory = 0
        if(client != null){
            for (AccountMovement move in client.accountancy.accountMovements) {
                if ((move.amount >= 0) && (move.appointment.id == appointment.id)) paymentHistory += move.amount
            }
        }
        else {
            for (Payment pay in appointment.payments){
                paymentHistory += pay.amount
            }
        }

        if (paymentHistory == appointment.totalToPay) throw new BadRequestException("Appointment already paid ")

        if (amount != null) {
            amountEntered = amount
        }

        if (appointment.payments == null) {
            appointment.payments = new ArrayList<>()
        }

        BigDecimal amountFromPoints = 0;

        if (points != null && client != null && client.points != null) {
            if (client.points >= points) {
                amountFromPoints = points / Integer.parseInt(Config.findByKey("changePurchase").value)
                client.points -= points
                //verify if there are a promotions with points
                Float pointFactor = verifyPointFactor(appointment.dayHour, appointment.services)
                Long newPoints = amountEntered.toInteger() * Integer.parseInt(Config.findByKey("changePay").value)  * pointFactor
                client.points += newPoints
                client.save()
            } else {
                throw new BadRequestException("The amount of client points is insufficient")
            }
        }

        totalAmount = amountEntered + amountFromPoints

        if(totalAmount == 0)   throw new BadRequestException("The amount to pay has to be different to zero")

        if (appointment.status != AppointmentStatus.PAID && appointment.status != AppointmentStatus.EXPIRED ) {
            if (paymentHistory + totalAmount > appointment.totalToPay) {
                throw new BadRequestException("The amount exceeds the remaining price difference of the pending or partial paid or exceeds the total price")
            }
            else if (totalAmount + paymentHistory == appointment.totalToPay)
                appointment.status = AppointmentStatus.PAID
            else
                appointment.status = AppointmentStatus.PARTIAL_PAID
        }
        else {
            throw new BadRequestException("The payment is already pay or expired")
        }

        Payment payment = new Payment()
        payment.appointment = appointment
        payment.amount = amountEntered
        payment.currency = currency
        payment.save()

        appointment.payments.add(payment)
        appointment.save()

        if (client != null) {
            AccountMovement accountMovement = new AccountMovement()
            accountMovement.appointment = appointment

            if (appointment.status == AppointmentStatus.PAID) {
                if(paymentHistory == 0)
                    accountMovement.amount = appointment.totalToPay
                else
                    accountMovement.amount = totalAmount
                client.accountancy.accountMovements.add(accountMovement)
            } else if (appointment.status == AppointmentStatus.PARTIAL_PAID) {
                if (client.accountancy.accountMovements.size() < 1) {

                    accountMovement.amount = -(appointment.totalToPay)
                    accountMovement.save()
                    client.accountancy.accountMovements.add(accountMovement)

                    AccountMovement movementPartialPaid = new AccountMovement()
                    movementPartialPaid.appointment = appointment
                    movementPartialPaid.amount = totalAmount
                    movementPartialPaid.save()
                    client.accountancy.accountMovements.add(movementPartialPaid)
                } else {
                    accountMovement.amount = totalAmount
                    accountMovement.save()

                    client.accountancy.accountMovements.add(accountMovement)
                }
            }
            client.save()
        }
        return appointment
    }

    private Float verifyDiscounts(Service service) {
        Float discount = 100
        Date now = new Date()
        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", now)
            gte("endDate", now)
            eq("status", PromotionStatus.ACTIVE)
            eq("type", PromotionType.DISCOUNT)
            services {
                eq("id", service.id)
            }
        }
        for (Promotion promotion : promotions) {
            discount = discount - promotion.discount
        }
        if (discount < 0) discount = 0
        return discount / 100
    }

    private Float verifyPointFactor(Date beginDate, Set<Service> servicesToVerify) {
        Float pointFactor = 1
        def ids = servicesToVerify.collect { element -> return element.id }
        def promotionCriteria = Promotion.createCriteria()
        List<Promotion> promotions = promotionCriteria.list {
            lte("startDate", beginDate)
            gte("endDate", beginDate)
            eq("status", PromotionStatus.ACTIVE)
            eq("type", PromotionType.POINT)
            services {
                inList("id", ids)
            }
        } as List<Promotion>
        for (Promotion promotion : promotions) {
            if (promotion.pointFactor != null) pointFactor = pointFactor * promotion.pointFactor
        }
        return pointFactor
    }
}
