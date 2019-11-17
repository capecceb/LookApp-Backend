package lookapp.backend

import grails.gorm.transactions.Transactional

@Transactional
class EmailService {
    def EmailService emailService

    def create(){


        String text = "La reserva del turno fue solicitada para el local: " +  "en el horario de : "
                + " Muchas gracias por elegirnos."
        
        emailService.sendMail {
            to "jerevallejo@gmail.com"
            from "alpaint011@gmail.com"
            subject "confirmación de reserva"
            body "sdasdasd"
        }


    }
}
