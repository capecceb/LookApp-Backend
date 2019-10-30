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

import java.text.SimpleDateFormat


@Integration
class PaymentSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    static SimpleDateFormat sdfCrud = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list payments"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/payments"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 0
    }

    void "test get a payment"() {
        def body=[:]

        when: 'I try get a list of appointments'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/payments/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().local == "casa"
    }

    void "test pay a appointment"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=1
        body["clientId"]=1
        body["appointmentId"]=1
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/1/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
    }

}
