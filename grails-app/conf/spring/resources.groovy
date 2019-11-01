import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import lookapp.backend.Appointment
import lookapp.backend.Client
import lookapp.backend.User
import lookapp.backend.Professional
import lookapp.backend.Branch

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

    AppointmentRenderer(JsonRenderer, Appointment) {
        includes = ['id', 'services', 'local', 'dayHour', 'status', 'client', 'professional']
    }

    AppointmentsRenderer(JsonCollectionRenderer, Appointment) {
        includes = ['id', 'services', 'local', 'dayHour', 'status', 'client', 'professional']
    }

    ClientRenderer(JsonRenderer, Client) {
        includes = ['id', 'name', 'lastName', 'DNI', 'primaryPhone', 'secondPhone', 'status', 'email']
    }

    ClientsRenderer(JsonCollectionRenderer, Client) {
        includes = ['id', 'name', 'lastName', 'DNI', 'primaryPhone', 'secondPhone', 'status', 'email']
    }

    BranchRenderer(JsonRenderer, Branch) {
    }
    BranchesRenderer(JsonCollectionRenderer, Branch) {
    }

}