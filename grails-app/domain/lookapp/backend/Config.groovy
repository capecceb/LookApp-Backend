package lookapp.backend

import grails.rest.Resource

@Resource(uri='/configs')
class Config {
    String key
    String value

    static mapping = {
        key column: "clave"
        value column: "valor"
    }
}

