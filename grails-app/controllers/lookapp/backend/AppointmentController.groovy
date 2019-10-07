package lookapp.backend

import java.text.SimpleDateFormat

class AppointmentController {

    static responseFormats = ['json', 'xml']

    static SimpleDateFormat sdfSearch = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    static SimpleDateFormat sdfCrud = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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
        if (params.dayHour == null) {
            res["message"] = "dayHour cant be null"
            respond(res, status: 400)
        }
        if (params.services == null) {
            res["message"] = "services cant be null"
            respond(res, status: 400)
        }
        if(params.local==null){
            res["message"] = "local cant be null"
            respond(res, status: 400)
        }
        Date beginDate = sdfCrud.parse(params.dayHour)
        Appointment appointment = new Appointment()

        try {
            appointment=appointmentService.save(appointment,params.local,beginDate,
                    params.services,params.client,params.professional)
        }catch(BadRequestException e) {
            res["message"]=e.message
            respond(res, status: 400)
            return
        }
        respond(appointment, status: 201)
9    }

    def update() {
        def res = [:]
        def params = request.getJSON()
        def queryParams = getParams()
        if (params.dayHour == null) {
            res["message"] = "dayHour cant be null"
            respond(res, status: 400)
        }
        if (params.services == null) {
            res["message"] = "services cant be null"
            respond(res, status: 400)
        }
        if(params.local==null){
            res["message"] = "local cant be null"
            respond(res, status: 400)
        }
        Date beginDate = sdfCrud.parse(params.dayHour)
        Appointment appointment = Appointment.get(queryParams.id)
        if(appointment==null){
            res["message"] = "Not Found"
            respond(res, status: 404)
        }
        try {
            appointment=appointmentService.save(appointment,params.local,beginDate,
                    params.services,params.client,params.professional)
        }catch(BadRequestException e) {
            res["message"]=e.message
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
        Date begin = sdfSearch.parse(params.beginDate)
        Date end = sdfSearch.parse(params.endDate)

        List<Professional> resultProfessionals = appointmentService.availableProfessionals(null,begin, end)
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
        }
        if (appointment.status != AppointmentStatus.OPEN) {
            res["message"] = "Invalid Status"
            respond(res, status: 400)
        }
        appointment.status = AppointmentStatus.PAID
        Appointment.withNewTransaction {
            appointment.save()
        }
        respond(appointment, status: 200)
    }
}
