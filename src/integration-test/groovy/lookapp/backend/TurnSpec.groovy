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
class TurnSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list turns"() {
        def body=[:]

        when: 'I try get a list of turns'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/turns"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].name == "casa"
    }

    void "test get a turn"() {
        def body=[:]

        when: 'I try get a list of turn'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/turns/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().local == "casa"
        response.body().dayHour == "2019-10-02T00:24:06Z"
    }
    void "test update turn"() {
        def body=[:]
        given: 'a changes for the turn'
        body["id"]=1
        body["local"]="casa1"

        when: 'I try update a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/services/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "teñido"
    }

    void "test update failed service"() {
        def body=[:]
        given: 'a changes for the service'
        body["id"]=3
        body["local"]="nolocal"
        body["dayHour"]="teñido"

        when: 'I try update a service'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/services/2",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add service"() {
        def body=[:]
        given: 'a new user'
        body["name"]="teñido"
        body["duration"]=240
        body["cost"]=300

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
        exception.message == "La propiedad [cost] de la clase [class lookapp.backend.Service] no puede ser nulo"
    }
}
