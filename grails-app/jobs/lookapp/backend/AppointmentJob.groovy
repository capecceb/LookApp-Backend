package lookapp.backend

import jline.internal.Log

class AppointmentJob {

    AppointmentService appointmentService

    static triggers = {
      simple repeatInterval: 60000l // execute job once in 5 seconds
    }

    def execute() {
        // execute job
        appointmentService.expireAppointments()
    }
}
