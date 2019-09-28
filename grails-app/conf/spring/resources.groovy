import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import lookapp.backend.User
// Place your Spring DSL code here
beans = {
    UserRenderer(JsonRenderer, User) {
        excludes = ['password']
    }

    UsersRenderer(JsonCollectionRenderer, User) {
        excludes = ['password']
    }
}
