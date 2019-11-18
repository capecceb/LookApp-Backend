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
class AppointmentStatusSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test cancel appointment"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/2/cancel",body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].status.name == "CANCELED"
    }
    void "test cancel failed appointment"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/1/cancel",body), List)
        response = client.toBlocking().exchange(HttpRequest.POST("/appointments/1/cancel",body), List)

        then: 'The result is ...'
        final HttpClientResponseException  exception = thrown()
        exception.message == "Invalid Status"
    }

    void "test cancel failed appointment not found"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/100/cancel",body), List)

        then: 'The result is ...'
        final HttpClientResponseException  exception = thrown()
        exception.message == "Not Found"
    }

    void "test set a appointment to pending of payment"() {
        def body=[:]

        when: 'I try set a appointment pending'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/3/pending",body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].status.name == "PENDING_PAID"
    }
    void "test paid failed appointment"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/2/pending",body), List)
        response = client.toBlocking().exchange(HttpRequest.POST("/appointments/2/pending",body), List)

        then: 'The result is ...'
        final HttpClientResponseException  exception = thrown()
        exception.message == "Invalid Status"
    }

    void "test paid failed appointment not found"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/100/paid",body), List)

        then: 'The result is ...'
        final HttpClientResponseException  exception = thrown()
        exception.message == "Not Found"
    }

}
