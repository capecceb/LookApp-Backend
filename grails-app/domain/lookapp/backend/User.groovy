package lookapp.backend
import grails.rest.Resource

@Resource(uri='/users')
class User {
    String username
    String password
    String fullname
    String email

    Date dateCreated
    Date lastUpdated

    static hasMany = [roles: Rol]
    static constraints = {
        fullname(nullable:true)
        password(nullable:true)
        email(nullable:true)
    }

    def beforeInsert() {
        password=password.encodeAsMD5()
    }

}
