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
        "/appointments/$id/pending"(controller: 'appointment', action:'pending',method:'POST')
        "/appointments/$id/pay"(controller: 'payment', action:'pay',method:'POST')
        "/appointments/search"(controller: 'appointment', action:'search',method:'GET')
        "/professionals/search"(controller: 'appointment', action:'searchProfessionals')
        "/payments"(controller: 'payment', action:'list',method:'GET')
        "/payments/$id"(controller: 'payment', action:'show',method:'GET')
        "/email"(controller: 'email', action:'send',method:'POST')

        "500"(controller: 'application', action:'error')
        "404"(view: '/notFound')
    }
}
