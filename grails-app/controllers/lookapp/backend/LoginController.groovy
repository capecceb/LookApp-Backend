package lookapp.backend


class LoginController {
    def login(){
        def res=[:]
        def params=request.getJSON()
        res["message"]="invalid username or password"
        if(params.email==null){
            respond(res,status: 401)
        }
        if(params.password==null){
            respond(res,status: 401)
        }
        User user=User.findByEmail(params.email)
        String password=params.password
        def passwordAsMD5 = password.encodeAsMD5()
        if(user==null || user.password!=passwordAsMD5) {
            respond(res,status: 401)
        } else {
            respond(user,status: 200)
        }
    }
    def cancel(){
        def params=getParams()
    }
}