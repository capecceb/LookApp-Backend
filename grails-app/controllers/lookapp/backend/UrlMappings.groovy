package lookapp.backend

class UrlMappings {

    static mappings = {
        "/"(controller: 'application', action:'index')
        "/login"(controller: 'login', action:'login',method:'POST')
        "/appointments"(controller: 'appointment', action:'list',method:'GET')
        "/appointments/$id"(controller: 'appointment', action:'show',method:'GET')
        "/appointments"(controller: 'appointment', action:'create',method:'POST')
        "/appointments/$id"(controller: 'appointment', action:'update',method:'PUT')
        "/appointments/$id/cancel"(controller: 'appointment', action:'cancel',method:'POST')
        "/appointments/$id/paid"(controller: 'appointment', action:'paid',method:'POST')
        "/professionals/search"(controller: 'appointment', action:'searchProfessionals')

        "500"(controller: 'application', action:'error')
        "404"(view: '/notFound')
    }
}
