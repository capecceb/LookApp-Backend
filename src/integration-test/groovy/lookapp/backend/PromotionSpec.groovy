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

        when: 'I try get a list of promotion'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        response.body()[0].name == "Descuentos"
    }

    void "test get a promotion"() {
        def body=[:]

        when: 'I try get a promotion'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Descuentos"
    }
    void "test update promotion"() {
        def body=[:]
        given: 'a changes for the promotion'
        body["name"]="Descuentos"
        body["status"]="ACTIVE"
        body["type"]="DISCOUNT"
        body["discount"]=20
        body["startDate"]="2018-01-01T14:00:00.000Z"
        body["endDate"]="2048-01-01T14:00:00.000Z"

        when: 'I try update a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/promotions/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Descuentos"
    }

    void "test update failed clients"() {
        def body=[:]
        given: 'a changes for the promotion'
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

        when: 'I try add a promotion'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/promotions",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Property [name] of class [class lookapp.backend.Promotion] cannot be null"
    }
    void "test search promotions without date"() {
        when: 'I try search a promotion'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions/search"), List)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "the date param is required"
    }

    void "test search promotions "() {
        when: 'I try search a promotion'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/promotions/search?date=2018-03-03T07:00"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 2
    }
}
