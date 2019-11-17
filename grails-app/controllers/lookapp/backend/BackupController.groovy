package lookapp.backend

import org.grails.web.json.JSONObject

class BackupController {

    static responseFormats = ['json', 'xml']
    ExportService exportService
    ImportService importService

    def getBackup() {
        respond(exportService.export(), status: 200)
    }

    def setBackup(){
        def params = request.getJSON()

        importService.deleteTables()
        importService.importData(params)

        respond(null, status: 200)
    }
}
