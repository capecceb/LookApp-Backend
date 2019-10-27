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
        respond(payment, status: 200)
    }

    def create() {
        def res = [:]
        def params = request.getJSON()
        String result = validate(params,["amount","appointmentId", "clientId", "points"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Payment payment = new Payment()

        try {
            payment = paymentService.save(payment, params.appointmentId, params.amoun, params.clientId, params.points )
        } catch (BadRequestException e) {
            res["message"] = e.message
            respond(res, status: 400)
            return
        }
        respond(payment, status: 201)
    }

    private String validate(def params,def verifyParams) {
        if (verifyParams.contains("appointmentId") && params.appointmentId == null) {
            return "appointmentId cant be null"
        }
        if (verifyParams.contains("clientId") && params.clientId == null) {
            return "clientId cant be null"
        }
        return null
    }
}
