package lookapp.backend

class ExportService {

    def export() {
        def res = [:]
        res["configs"] = Config.findAll()
        res["branches"] = Branch.findAll()
        res["services"] = Service.findAll()
        res["payments"] = Payment.findAll()
        res["workingHours"] = WorkingHour.findAll()
        res["clients"] = Client.findAll()
        res["roles"] = Rol.findAll()

        def list = []
        for (User user : User.findAll()) {
            def map = [:]
            map = user
            map["roles"] = user.roles
            list.add(map)
        }
        res["users"] = list
        list = []
        for (Appointment appointment : Appointment.findAll()) {
            def map = [:]
            map = appointment
            map["services"] = appointment.services
            map["payments"] = appointment.payments
            map["promotions"] = appointment.promotions
            list.add(map)
        }
        res["appointments"] = list
        list = []
        for (Professional professional : Professional.findAll()) {
            def map = [:]
            map = professional
            map["services"] = professional.services
            map["workingHours"] = professional.workingHours
            list.add(map)
        }
        res["professionals"] = list
        list = []
        for (Professional professional : Professional.findAll()) {
            def map = [:]
            map = professional
            map["services"] = professional.services
            map["workingHours"] = professional.workingHours
            list.add(map)
        }
        res["professionals"] = list
        list = []
        for (Promotion promotion : Promotion.findAll()) {
            def map = [:]
            map = promotion
            map["services"] = promotion.services
            list.add(map)
        }
        res["promotions"] = list
        return res
    }
}
