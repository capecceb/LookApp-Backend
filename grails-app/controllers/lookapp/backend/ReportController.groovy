package lookapp.backend

class ReportController {

    static responseFormats = ['json', 'xml']

    def reportService

    def listProfessional() {
        def res = [:]
        def params = request.getJSON()

        List<ProfessionalReport> reports
        Date fromDate = null
        Date toDate = null
        if(params.fechaDesde !=null ){
            fromDate = DateTimeParser.parse(params.fromDate)
        }
        if(params.fechaHasta !=null ){
            toDate = DateTimeParser.parse(params.toDate)
        }
        try {
            reports = reportService.generate(fromDate, toDate, params.branchId)
        } catch (Exception e) {
            res["message"] = e.message
            respond(res, status: 500)
            return
        }
        respond(reports, status: 200)
    }
}
