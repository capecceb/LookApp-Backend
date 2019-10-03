package lookapp.backend

class UrlMappings {

    static mappings = {
        "/"(controller: 'application', action:'index')
        "/login"(controller: 'default', action:'login',method:'POST')


        "500"(controller: 'application', action:'error')
        "404"(view: '/notFound')
    }
}
