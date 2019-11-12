package lookapp.backend

class ServiceReportController {

    static responseFormats = ['json', 'xml']

    def serviceReportService

    def listService() {
        def res = [:]
        def params = request.getJSON()

        List<ServiceReport> reports
        Date fromDate = null
        Date toDate = null
        if(params.fromDate !=null ){
            fromDate = DateTimeParser.parse(params.fromDate)
        }
        if(params.toDate !=null ){
            toDate = DateTimeParser.parse(params.toDate)
        }
        try {
            reports = serviceReportService.generate(fromDate, toDate, params.branchId)
        } catch (Exception e) {
            res["message"] = e.message
            respond(res, status: 500)
            return
        }
        respond(reports, status: 200)
    }
}
