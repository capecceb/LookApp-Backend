package lookapp.backend

class BackupController {

    static responseFormats = ['json', 'xml']

    def getBackup() {
        def res = [:]
        res["appointments"] = Appointment.findAll()
        res["branches"] = Branch.findAll()
        res["clients"] = Client.findAll()
        res["configs"] = Config.findAll()
        res["payments"] = Payment.findAll()
        res["professionals"] = Professional.findAll()
        res["professionalReports"] = ProfessionalReport.findAll()
        res["promotions"] = Promotion.findAll()
        res["roles"] = Rol.findAll()
        res["services"] = Service.findAll()
        res["serviceReports"] = ServiceReport.findAll()
        res["users"] = User.findAll()
        res["workingHours"] = WorkingHour.findAll()
        respond(res, status: 200)
    }

    def setBackup(){
        def params = request.getJSON()
        if(params.appointments){
            Appointment.withNewTransaction {
                Appointment.deleteAll(Appointment.findAll())
            }
            for(Appointment appointment:params.appointments){
                appointment.save()
            }
        }
        if(params.branches){
            Branch.withNewTransaction {
                Branch.deleteAll(Branch.findAll())
            }
            for(Branch branch:params.branches){
                branch.save()
            }
        }

        respond(null, status: 200)
    }
}
