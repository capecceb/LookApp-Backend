package lookapp.backend


import grails.rest.*
import grails.converters.*

class DefaultController {
    def ping() {
        render "pong"
    }

    def json() {
        def a=[:]
        a["json"]="test"
        respond (a, status: 200)
    }

    def post() {
        respond ([:], status: 201)
    }
}
