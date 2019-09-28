package lookapp.backend


class DefaultController {
    def login(){
        def res=[:]
        def params=request.getJSON()
        res["message"]="invalid username or password"
        if(params.username==null){
            respond(res,status: 401)
        }
        if(params.password==null){
            respond(res,status: 401)
        }
        User user=User.findByUsername(params.username)
        String password=params.password
        def passwordAsMD5 = password.encodeAsMD5()
        if(user==null || user.password!=passwordAsMD5) {
            respond(res,status: 401)
        } else {
            respond(user,status: 200)
        }
    }
}
