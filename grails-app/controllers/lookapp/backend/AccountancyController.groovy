package lookapp.backend

class AccountancyController {

    static responseFormats = ['json', 'xml']

    private show(Integer clientId){

        Client client = Client.get(clientId)
        List<AccountMovement> accountMovements = client.accountancy.accountMovements
        List<AccountMovement> accountMovements = client.accountancy.accountMovement
        respond(accountMovements, status: 200)
    }

}
