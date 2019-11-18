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
class AccountancySpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list accountancy"() {
        def body=[:]

        when: 'I try get a list of accountancy'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/accountancy"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 8
    }

    void "test get a accountancy"() {
        def body=[:]

        when: 'I try get a accountancy'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().id == 1
    }

    void "test generate movement in accountancy in a pay"(){
        def body=[:]

        given: "A new payment"
        body["amount"]= 100.00
        body["appointmentId"]=13
        body["clientId"]=4
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/13/pay",body), Map)

        response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/4"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().accountMovements.size() > 0
        response.body().accountMovements[0].amount == 100.00
    }

    void "test generate movements in accountancy in a partial pay"(){
        def body=[:]

        given: "A new payment"
        body["amount"]= 50.00
        body["appointmentId"]=14
        body["clientId"]=5
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/14/pay",body), Map)

        response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/5"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().accountMovements.size() == 2
        def total = 0
        for(AccountMovement move in (response.body().accountMovements as List<AccountMovement>)){
            total += move.amount
        }
        total == -50
    }

    void "test generate movement in accountancy in a pending pay"(){
        def body=[:]

        when: 'I make a new pending pay'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/15/pending",body), Map)

        response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/6"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().accountMovements[0].amount == -100.00
    }

    void "test generate wrong movement in accountancy in a pending pay"(){
        def body=[:]

        when: 'I make a new pending pay'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/16/pending",body), Map)

        response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/7"), Map)

        then: 'The result is ...'
        response.status().code == 200
        !(response.body().accountMovements[0].amount != -100.00)
    }

    void "test generate wrong movements in accountancy in a partial pay"(){
        def body=[:]

        given: "A new payment"
        body["amount"]= 50.00
        body["appointmentId"]=17
        body["clientId"]=8
        body["currency"]="ARS"
        when: 'I try pay a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/17/pay",body), Map)

        response = client.toBlocking().exchange(HttpRequest.GET("/accountancy/8"), Map)

        then: 'The result is ...'
        response.status().code == 200
        def total = 0
        for(AccountMovement move in (response.body().accountMovements as List<AccountMovement>)){
            total += move.amount
        }
        !(total != -50)
    }

    void "test pay appointmnet with no client"(){
        def body=[:]

        given: "A new payment"
        body["amount"]= 100
        body["appointmentId"]=18
        body["currency"]="ARS"
        when: 'I try to pay an a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/18/pay",body), Map)

        then: 'The result is ...'
        response.status().code == 201
    }

}
