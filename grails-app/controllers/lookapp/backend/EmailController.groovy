package lookapp.backend

import java.text.SimpleDateFormat


class EmailController {

    static responseFormats = ['json', 'xml']

    def MailService

    def send() {

        def res = [:]
        def params = request.getJSON()

        try {
            Appointment appointment = Appointment.get(params.appointmentId)
            SimpleDateFormat fech = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat hour = new SimpleDateFormat("HH:mm");
            String dateString = fech.format( appointment.dayHour   );
            String hourString = hour.format( appointment.dayHour   );
            String payDateString = ""
            String payHourString = ""
            String local = appointment.branch.name
            BigDecimal amount = 0

            if(params.paymentId != null){
                Payment payment = Payment.get(params.paymentId)
                amount = payment.amount
                SimpleDateFormat payFech = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat payHour = new SimpleDateFormat("HH:mm");
                payDateString = payFech.format( payment.dateCreated   );
                payHourString = payHour.format( payment.dateCreated   );

            }

            Client client = Client.get(params.clientId)
            String clientName = client.name +" "+ client.lastName
            String mail = client.email

            String bodyText
            String subjectText

            if(params.id ==1)
            {
                bodyText = "Hola "+clientName + "\nSe ha registrado de forma exitosa su reserva de turno. \nPara el dia "+dateString +" a la hora: "+ hourString +"\nEn el local: "+local+"\n\nMuchas gracias por elegirnos\n\nSaludos Hair&Head"
                subjectText = "Reserva de turno"
            }
            if(params.id ==2)
            {
                bodyText = "Hola "+clientName + "\nSe ha modificado de forma exitosa su reserva de turno. \nPara el dia "+dateString +" a la hora: "+ hourString +"\nEn el local: "+local+"\n\nMuchas gracias por elegirnos\n\nSaludos Hair&Head"
                subjectText = "Modificacion de turno"
            }
            if(params.id ==3)
            {
                bodyText = "Hola "+clientName + "\nSe ha cancelado su reserva de turno. \nPara el dia "+dateString +" a la hora: "+ hourString +"\nEn el local: "+local+"\n\nMuchas gracias por elegirnos\n\nSaludos Hair&Head"
                subjectText = "Cancelacion de turno"
            }
            if(params.id ==4)
            {
                bodyText = "Hola "+clientName + "\nSe ha registrado un pago para su turno del dia "+dateString +"\nEn el local: "+local+"\nEl monto abanodo es de: \$"+ amount+"\nLa fecha que se registro el pago fue el dia "+payDateString +" a la hora "+payHourString+"\n\nMuchas gracias por elegirnos\n\nSaludos Hair&Head"
                subjectText = "Comprobante de pago"
            }
            MailService.sendMail {
                to mail
                subject subjectText
                body bodyText
            }

        } catch (Exception e) {
            res["message"] = e.message
            respond(res, status: 500)
            return
        }
        respond(status: 200)
    }
}