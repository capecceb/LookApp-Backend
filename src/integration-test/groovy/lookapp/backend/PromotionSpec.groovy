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
class PromotionSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list promotions"() {
        def body=[:]

        when: 'I try get a list of clients'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        response.body()[0].name == "Descuentos"
    }

    void "test get a promotion"() {
        def body=[:]

        when: 'I try get a list of clients'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Descuentos"
    }
    void "test update promotion"() {
        def body=[:]
        given: 'a changes for the promotion'
        body["id"]=1
        body["name"]="test"

        when: 'I try update a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/promotions/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "test"
    }

    void "test update failed clients"() {
        def body=[:]
        given: 'a changes for the client'
        body["id"]=5
        body["name"]="noExiste"

        when: 'I try update a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/promotions/5",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add promotion "() {
        def body=[:]
        given: 'a new promotion'
        body["name"]="test"
        body["status"]="ACTIVE"
        body["type"]="DISCOUNT"
        body["discount"]=10
        body["startDate"]="2019-10-07T14:00:00.000Z"
        body["endDate"]="2019-10-08T14:00:00.000Z"
        body["services"] = [1]

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/promotions",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().name == "test"
    }

    void "test add failed promotion "() {
        def body=[:]
        given: 'a new promotion'
        body["status"]="ACTIVE"
        body["type"]="DISCOUNT"
        body["discount"]=10
        body["startDate"]="2019-10-07T14:00:00.000Z"
        body["endDate"]="2019-10-08T14:00:00.000Z"
        body["services"] = [1]

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/promotions",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "La propiedad [name] de la clase [class lookapp.backend.Promotion] no puede ser nulo"
    }
}