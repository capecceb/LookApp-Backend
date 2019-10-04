package lookapp.backend

import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.net.http.HttpClient
import java.net.http.HttpResponse

@Integration
class ProfessionalSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list professionals"() {
        def body=[:]

        when: "I try to get a list of professionals"
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/professionals"), List)

        then: "The result is..."
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].name == "Nicolas"

    }


}
