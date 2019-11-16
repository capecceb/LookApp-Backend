package lookapp.backend

class AppointmentJob {

    AppointmentService appointmentService
    static boolean isRun = false
    static triggers = {
        simple repeatInterval: 60000l // execute job once in 5 seconds
    }

    static void run() {
        this.isRun = true
    }

    def execute() {
        // execute job
        if(this.isRun) appointmentService.expireAppointments()
    }
}
