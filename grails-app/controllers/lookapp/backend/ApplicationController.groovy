package lookapp.backend

import grails.core.GrailsApplication
import grails.plugins.*

class ApplicationController implements PluginManagerAware {

    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    def index() {
        [grailsApplication: grailsApplication, pluginManager: pluginManager]
    }

    def index2() {
        def res= [grailsApplication: grailsApplication, pluginManager: pluginManager]
//        res["test"]="internal server error"
        respond (res, status: 200)
    }
}
