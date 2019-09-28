package lookapp.backend


class DefaultController {


    def login(){
        def params=request.getJSON()
        if(params.username==null){
            throw new RuntimeException("username ")
        }
        if(params.password==null){
            throw new RuntimeException("invalid password")
        }
        User user=User.findByUsername(params.username)
        def res=[:]
        if(user==null || user.password!=params.password)
        {
            res["login"]="nok"
        } else {
            res["login"]="ok"
        }
        respond(res,status: 200)
    }

    def test() {
        Rol rol=Rol.findByName("Supervisor")
        User user=new User(name:"test")
        user.roles=new ArrayList()
        user.roles.add(rol)
        rol.withNewTransaction {
            user.save(flush: true, failOnError: true)
       }
        respond (User.list(), status: 200)
    }

    def error() {

        def exception = request.getAttribute('exception')

        def res=[:]
        if(exception!=null) {
            res["message"] = "internal server error"
            res["cause"] = exception.cause.toString()
            res["target"] = exception.cause.target.toString()
        }
        respond (res, status: 500)
    }
}
