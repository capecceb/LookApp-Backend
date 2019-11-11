package lookapp.backend

class PaymentController {

    static responseFormats = ['json', 'xml']

    def paymentService

    def list() {
        List<Payment> payments = Payment.list()
        respond(payments, status: 200)
    }

    def show() {
        def params = getParams()
        Payment payment = Payment.get(params.id)
        respond(payment, status: payment != null ? 200 : 404)
    }

    def pay() {
        def res = [:]
        def params = request.getJSON()
        String result = validate(params, ["currency", "appointmentId"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Appointment appointment
        try {
            appointment = paymentService.save(params.appointmentId as Long, params.amount as BigDecimal, params.currency as String, params.clientId as Long, params.points as Integer)
        } catch (Exception e) {
            res["message"] = e.message
            respond(res, status: 500)
            return
        }
        respond(appointment, status: 201)
    }

    private String validate(def params, def verifyParams) {
        if (verifyParams.contains("appointmentId") && params.appointmentId == null) {
            return "appointmentId cant be null"
        }
        return null
    }
}
