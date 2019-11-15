package lookapp.backend


class EmailController {

    static responseFormats = ['json', 'xml']

   // def MailService emailService

    def MailService

    def create() {

        def res = [:]
        def params = request.getJSON()


        try {
            String txt = "se envia desde jere?"

            MailService.sendMail {
                to "jerevallejo@gmail.com"
                from "jerevallejo@gmail.com"
                subject "confirmacion de reserva"
                body txt
            }

        } catch (Exception e) {
            res["message"] = e.message
            respond(res, status: 500)
            return
        }

        respond(status: 200)
    }

    private String validate(def params, def verifyParams) {
        if (verifyParams.contains("appointmentId") && params.appointmentId == null) {
            return "appointmentId cant be null"
        }
        if (verifyParams.contains("clientId") && params.clientId == null) {
            return "clientId cant be null"
        }
        return null
    }
}
//tengo que ver donde envio el mail, en el controller de appointment?
//en el service de appointment o dejo un endpoint para enviar un mail?c