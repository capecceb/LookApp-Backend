package lookapp.backend
import grails.rest.Resource

@Resource(uri='/users')
class User {
    String email
    String password
    String fullName

    Date dateCreated
    Date lastUpdated

    static hasMany = [roles: Rol]
    static constraints = {
        fullName(nullable:true)
        password(nullable:true)
    }

    def beforeInsert() {
        password=password.encodeAsMD5()
    }

}
