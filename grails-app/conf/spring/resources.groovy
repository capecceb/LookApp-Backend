import grails.rest.render.json.JsonRenderer
import grails.rest.render.json.JsonCollectionRenderer
import lookapp.backend.Appointment
import lookapp.backend.Client
import lookapp.backend.ProfessionalReport
import lookapp.backend.Service
import lookapp.backend.ServiceReport
import lookapp.backend.User
import lookapp.backend.Professional
import lookapp.backend.Branch

// Place your Spring DSL code here
beans = {
    UserRenderer(JsonRenderer, User) {
    }

    UsersRenderer(JsonCollectionRenderer, User) {
        excludes = ['password']
    }

    ProfessionalRenderer(JsonRenderer, Professional) {
    }

    ProfessionalsRenderer(JsonCollectionRenderer, Professional) {
    }

    AppointmentRenderer(JsonRenderer, Appointment) {
    }

    AppointmentsRenderer(JsonCollectionRenderer, Appointment) {
    }

    ClientRenderer(JsonRenderer, Client) {
    }

    ClientsRenderer(JsonCollectionRenderer, Client) {
    }

    BranchRenderer(JsonRenderer, Branch) {
    }

    BranchesRenderer(JsonCollectionRenderer, Branch) {
    }

    ProfessionalReportRenderer(JsonRenderer, ProfessionalReport) {
    }

    ProfessionalReportsRenderer(JsonCollectionRenderer, ProfessionalReport) {
    }

    ServiceReportRenderer(JsonRenderer, ServiceReport) {
    }

    ServiceReportsRenderer(JsonCollectionRenderer, ServiceReport) {
    }

    ServiceRenderer(JsonRenderer, Service) {
    }

    ServiceRenderer(JsonCollectionRenderer, Service) {
    }

}
