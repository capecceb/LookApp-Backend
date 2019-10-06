package lookapp.backend

class UrlMappings {

    static mappings = {
        "/"(controller: 'application', action:'index')
        "/login"(controller: 'login', action:'login',method:'POST')
       "/appointments/$id/cancel"(controller: 'appointment', action:'cancel')

        "500"(controller: 'application', action:'error')
        "404"(view: '/notFound')
    }
}
