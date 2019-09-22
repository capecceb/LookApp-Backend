package lookapp.backend

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "/ping"(controller: 'default', action:'ping')
        "/json"(controller: 'default', action:'json',method:'GET')
        "/json"(controller: 'default', action:'post',method:'POST')

    }
}
