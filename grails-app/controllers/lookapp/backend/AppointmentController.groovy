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
        String result = validate(params,["dayHour","services","local","branch"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Date beginDate = DateTimeParser.parse(params.dayHour)
        Appointment appointment = new Appointment()

        try {
            appointment = appointmentService.save(appointment, params.local, beginDate,
                    params.services, params.client, params.professional,params.branch)
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
        String result = validate(params,["dayHour","services","local","branch"])
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
                    params.services, params.client, params.professional,params.branch)
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
        String result = validate(params,["beginDate","endDate"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
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

    def search(){
        def res = [:]
        def params = getParams()
        String result = validate(params,["professional"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Date begin
        if(params.beginDate!=null){
            begin = DateTimeParser.parseSearchFormat(params.beginDate)
        }
        Date end
        if(params.endDate!=null){
            end = DateTimeParser.parseSearchFormat(params.endDate)
        }
        Long professional = params.professional as Long
        List<Professional> resultProfessionals = appointmentService.searchAppointments(professional,begin, end)
        respond(resultProfessionals, status: 200)
    }

    private String validate(def params,def verifyParams) {
        if (verifyParams.contains("dayHour") && params.dayHour == null) {
            return "dayHour cant be null"
        }
        if (verifyParams.contains("services") && params.services == null) {
            return "services cant be null"
        }
        if (verifyParams.contains("local") && params.local == null) {
            return "local cant be null"
        }
        if (verifyParams.contains("beginDate") && params.beginDate == null) {
            return "Invalid begin date"
        }
        if (verifyParams.contains("endDate") && params.endDate == null) {
            return "Invalid end date"
        }
        if(verifyParams.contains("professional") && params.professional==null){
            return "Invalid professional id"
        }
        return null
    }
}
