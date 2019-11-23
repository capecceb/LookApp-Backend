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
        response.body().size() == 8
    }

    void "test get a payment"() {
        def body=[:]

        when: 'I try get a list of appointments'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/payments/9"), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test partial pay a appointment"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=50
        body["appointmentId"]=19
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/19/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PARTIAL_PAID"
    }
    void "test pay a appointment"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=250
        body["clientId"]=2
        body["appointmentId"]=2
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/2/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PAID"
    }

    void "try to pay an appointment, with points and cash"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=125
        body["points"]=50
        body["clientId"]=2
        body["appointmentId"]=3
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/3/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PAID"
    }

    void "try to pay an appointment, with points"() {
        def body=[:]
        given: 'a new appointment'
        body["points"]=300
        body["clientId"]=2
        body["appointmentId"]=4
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/4/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PAID"
    }

    void "test failed payment invalid appointment"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=100
        body["points"]=50
        body["clientId"]=2
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/100/pay",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "appointmentId cant be null"
    }

    void "test partial pay a appointment, with points"() {
        def body=[:]
        given: 'a new appointment'
        body["points"]=100
        body["clientId"]=2
        body["appointmentId"]=5
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/5/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PARTIAL_PAID"
    }

    void "test pay a appointment, with points is insufficient"() {
        def body=[:]
        given: 'a new appointment'
        body["points"]=150
        body["clientId"]=3
        body["appointmentId"]=6
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/6/pay",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "The amount of client points is insufficient"
    }

    void "test pay a appointment, payment exceeds cost"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=300
        body["clientId"]=3
        body["appointmentId"]=6
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/6/pay",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "The amount exceeds the remaining price difference of the pending or partial paid or exceeds the total price"
    }

    void "test pay a appointment, with a empty client"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=150
        body["appointmentId"]=7
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/7/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().status["name"] == "PAID"
    }

    void "test pay a appointment, with a empty client and points"() {
        def body=[:]
        given: 'a new appointment'
        body["amount"]=100
        body["points"]=50
        body["appointmentId"]=7
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/7/pay",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Appointment already paid "
    }
}
