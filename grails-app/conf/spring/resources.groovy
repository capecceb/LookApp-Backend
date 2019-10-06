import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import lookapp.backend.Appointment
import lookapp.backend.Client
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

    AppointmentRenderer(JsonRenderer, Appointment) {
        includes = ['id','services','local','dayHour','status','client']
    }

    AppointmentsRenderer(JsonCollectionRenderer, Appointment) {
        includes = ['id','services','local','dayHour','status','client']
    }

    ClientRenderer(JsonRenderer, Client) {
        includes = ['id','name','surname','DNI','primaryPhone','secondPhone']
    }

    ClientRenderer(JsonCollectionRenderer, Client) {
        includes = ['id','name','surname','DNI','primaryPhone','secondPhone']
    }
}
