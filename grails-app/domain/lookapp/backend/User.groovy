package lookapp.backend
import grails.rest.Resource

@Resource(uri='/users')
class User {
    String email
    String password
    String name
    String lastName
    UserStatus status
    Branch branch

    Date dateCreated
    Date lastUpdated

    static hasMany = [roles: Rol]
    static constraints = {
        name(nullable:true)
        lastName(nullable:true)
        password(nullable:true)
        branch(nullable:true)
        email(unique:true)
    }

    static mapping = {
        email index:'email_idx'
    }
}
