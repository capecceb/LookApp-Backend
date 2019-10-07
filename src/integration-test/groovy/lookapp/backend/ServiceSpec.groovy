package lookapp.backend


import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification


@Integration
class ServiceSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list services"() {
        def body=[:]

        when: 'I try get a list of services'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/services"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        response.body()[0].name == "corte"
    }

    void "test get a service"() {
        def body=[:]

        when: 'I try get a list of service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/services/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "corte"
    }
    void "test update service"() {
        def body=[:]
        given: 'a changes for the service'
        body["id"]=1
        body["name"]="teñido"

        when: 'I try update a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/services/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "teñido"
    }

    void "test update failed service"() {
        def body=[:]
        given: 'a changes for the service'
        body["id"]=2
        body["name"]="teñido"

        when: 'I try update a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/services/200",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add service"() {
        def body=[:]
        given: 'a new user'
        body["name"]="teñido"
        body["duration"]=240
        body["price"]=300

        when: 'I try add a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/services",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().name == "teñido"
    }

    void "test add failed service"() {
        def body=[:]
        given: 'a empty service without cost'
        body["name"]="teñido"
        body["duration"]=240

        when: 'I try add a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/services",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "La propiedad [price] de la clase [class lookapp.backend.Service] no puede ser nulo"
    }
}
