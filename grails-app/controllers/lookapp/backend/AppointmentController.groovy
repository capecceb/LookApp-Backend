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
        String result = validate(params,["beginDate","endDate","branch"])
        if (result != null) {
            res["message"] = result
            respond(res, status: 400)
            return
        }
        Branch branch=Branch.get(params.branch)
        if(branch==null){
            res["message"] = "branch not found"
        }
        Date begin = DateTimeParser.parseSearchFormat(params.beginDate)
        Date end = DateTimeParser.parseSearchFormat(params.endDate)

        List<Professional> resultProfessionals = appointmentService.availableProfessionals(null, begin, end,branch)
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

    def pending() {
        def res = [:]
        def params = getParams()
        BigDecimal totalCost = 0;
        def services = new ArrayList()
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
        appointment.status = AppointmentStatus.PENDING_PAID
        Appointment.withNewTransaction {
            appointment.save()
        }
        if (appointment.client != null) {
            AccountMovement accountMovement = new AccountMovement()
            accountMovement.appointment = appointment
            accountMovement.amount = -(appointment.totalToPay)
            AccountMovement.withNewTransaction {
                accountMovement.save()
            }

            Client client = Client.get(appointment.client.id)
            client.accountancy.accountMovements.add(accountMovement)
            Client.withNewTransaction {
                client.save()
            }
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

    def expire(){
        appointmentService.expireAppointments()
        respond(null, status: 200)
    }

    private Client getClient(Integer clientId){
        Client client = Client.get(clientId)
        if (client == null) {
            throw new BadRequestException("Invalid client id")
        }
        return client
    }

    private Professional getProfessional(Integer professionalId,Day day,Integer beginDate,Integer endDate){
        Professional professional = Professional.get(professionalId)
        if (professional == null) {
            throw new BadRequestException("Invalid professional id")
        }
        if (professional.status != ProfessionalStatus.ACTIVE) {
            throw new BadRequestException("Error the professional isn't active")
        }
        boolean isWorking=false
        for(WorkingHour workingHour:professional.workingHours){
            if(workingHour.days==day){
                if(workingHour.beginHour<=beginHour && workingHour.endHour>endHour){
                    isWorking=true
                }
            }
        }
        if(!isWorking){
            throw new BadRequestException("The professional does not work at that time")
        }
        def criteria = Appointment.createCriteria()
        List<Appointment> appointmentList = criteria.list {
            lt("dayHour", endDate)
            gt("endDate", beginDate)
            eq("status", AppointmentStatus.OPEN)
            eq("professional", professional)
            if (appointment.id != null) {
                ne("id", appointment.id)
            }
        }
        if (appointmentList.size() > 0) {
            throw new BadRequestException("professional is busy")
        }
        return professional
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
