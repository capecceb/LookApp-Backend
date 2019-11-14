package lookapp.backend

class AccountancyController {

    static responseFormats = ['json', 'xml']

    private show(Integer clientId){
        def res = [:]
        Client client = Client.get(clientId)
        if(client == null){
            res["message"] = "Client not found"
            respond(res, status: 400)
            return
        }
        try {
            List<AccountMovement> accountMovements = client.accountancy.accountMovements
            respond(accountMovements, status: 200)
        }
        catch(Exception e){
            res["message"] = e.message
            respond(res, status: 500)
            return
        }
    }

}
