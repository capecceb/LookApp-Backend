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
class AccountancySpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client


    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list account movements"() {
        def body=[:]

        when: 'I try get a list of account movements'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/accountancy"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        Accountancy cuenta = response.body()[1] as Accountancy
        println("amount: " + cuenta.accountMovements[0].amount )
        response.body()[1].accountMovements[0].amount == 400
    }

    void "Generate correct account movement when pay an a appointment"(){
        def body=[:]
        given: 'a new account movement'
        body["clientId"] = 1
        when: 'I try to generate a new account movement'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments/1",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body()[0].accountMovements[0].appointmentId == 1
        response.body()[0].accountMovements[0].amount == 150
    }
}
