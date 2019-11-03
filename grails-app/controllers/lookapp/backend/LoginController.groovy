package lookapp.backend


class LoginController {
    def login(){
        def res=[:]
        def params=request.getJSON()
        res["message"]="invalid username or password"
        if(params.email==null){
            respond(res,status: 401)
            return
        }
        if(params.password==null){
            respond(res,status: 401)
            return
        }
        User user=User.findByEmailAndStatus(params.email,UserStatus.ACTIVE)
        if(user==null || user.password!=params.password) {
            respond(res,status: 401)
            return
        }
        respond(user,status: 200)
    }
}
