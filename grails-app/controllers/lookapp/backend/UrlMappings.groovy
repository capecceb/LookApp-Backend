package lookapp.backend

class UrlMappings {

    static mappings = {
        "500"(controller: 'default', action:'error')

        "/"(controller: 'application', action:'index2')
        "/test"(controller: 'default', action:'test',method:'GET')
        "/login"(controller: 'default', action:'login',method:'POST')
    }
}
