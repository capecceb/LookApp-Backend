import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import lookapp.backend.User
import lookapp.backend.Professional
// Place your Spring DSL code here
beans = {
    UserRenderer(JsonRenderer, User) {
        excludes = ['password']
    }

    UsersRenderer(JsonCollectionRenderer, User) {
        excludes = ['password']
    }

    ProfessionalRenderer(JsonRenderer, Professional) {
    }

    ProfessionalsRenderer(JsonCollectionRenderer, Professional) {
    }
}
