package lookapp.backend

class AppointmentController {

    static responseFormats = ['json', 'xml']

    def appointmentService

    def list() {
        List<Appointment> appointments = Appointment.list()
        respond(appointments, status: 200)
    }

    def show() {
        def params = getParams()
        Appointment appointment = Appointment.get(params.id)
        respond(appointment, status: 200)
    }

    def create() {
        def res = [:]
        def params = request.getJSON()
        String result = validateSave(params)
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Date beginDate = DateTimeParser.parse(params.dayHour)
        Appointment appointment = new Appointment()

        try {
            appointment = appointmentService.save(appointment, params.local, beginDate,
                    params.services, params.client, params.professional)
        } catch (BadRequestException e) {
            res["message"] = e.message
            respond(res, status: 400)
            return
        }
        respond(appointment, status: 201)
    }

    def update() {
        def res = [:]
        def params = request.getJSON()
        def queryParams = getParams()
        String result = validateSave(params)
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Date beginDate = DateTimeParser.parse(params.dayHour)
        Appointment appointment = Appointment.get(queryParams.id)
        if (appointment == null) {
            res["message"] = "Not Found"
            respond(res, status: 404)
            return
        }
        try {
            appointment = appointmentService.save(appointment, params.local, beginDate,
                    params.services, params.client, params.professional)
        } catch (BadRequestException e) {
            res["message"] = e.message
            respond(res, status: 400)
            return
        }
        respond(appointment, status: 200)
    }

    def searchProfessionals() {
        def res = [:]
        def params = getParams()
        if (params.beginDate == null) {
            res["message"] = "Invalid begin date"
            respond(res, status: 400)
        }
        if (params.endDate == null) {
            res["message"] = "Invalid end date"
            respond(res, status: 400)
        }
        Date begin = DateTimeParser.parseSearchFormat(params.beginDate)
        Date end = DateTimeParser.parseSearchFormat(params.endDate)

        List<Professional> resultProfessionals = appointmentService.availableProfessionals(null, begin, end)
        respond(resultProfessionals, status: 200)
    }

    def cancel() {
        def res = [:]
        def params = getParams()
        Appointment appointment = Appointment.get(params.id)
        if (appointment == null) {
            res["message"] = "Not Found"
            respond(res, status: 404)
            return
        }
        if (appointment.status != AppointmentStatus.OPEN) {
            res["message"] = "Invalid Status"
            respond(res, status: 400)
            return
        }
        appointment.status = AppointmentStatus.CANCELED
        Appointment.withNewTransaction {
            appointment.save()
        }
        respond(appointment, status: 200)
    }

    def paid() {
        def res = [:]
        def params = getParams()
        Appointment appointment = Appointment.get(params.id)
        if (appointment == null) {
            res["message"] = "Not Found"
            respond(res, status: 404)
            return
        }
        if (appointment.status != AppointmentStatus.OPEN) {
            res["message"] = "Invalid Status"
            respond(res, status: 400)
            return
        }
        appointment.status = AppointmentStatus.PAID
        Appointment.withNewTransaction {
            appointment.save()
        }
        respond(appointment, status: 200)
    }
    private String validateSave(def params) {
        if (params.dayHour == null) {
            return "dayHour cant be null"
        }
        if (params.services == null) {
            return "services cant be null"
        }
        if (params.local == null) {
            return "local cant be null"
        }
        return null
    }
}
