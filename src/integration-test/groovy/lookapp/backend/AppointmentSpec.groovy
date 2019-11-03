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
class AppointmentSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    static SimpleDateFormat sdfCrud = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list appointment"() {
        def body=[:]

        when: 'I try get a list of appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/appointments"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 8
        response.body()[0].local == "casa"
    }

    void "test get a appointments"() {
        def body=[:]

        when: 'I try get a list of appointments'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/appointments/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().local == "casa"
    }
    void "test add appointment"() {
        def body=[:]
        given: 'a new appointment'
        body["local"]="turnNew"
        body["client"]=1
        body["status"]="OPEN"
        body["dayHour"]= "2019-10-07T14:00:00.000Z"
        body["services"]=[1]
        when: 'I try add a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments",body), Map)

        then: 'The result is ...'
        response.status().code == 201
    }
    void "test update appointment"() {
        def body=[:]
        given: 'a changes for the appointment'
        body["id"]=1
        body["local"]="editado"
        body["dayHour"]= "2019-10-07T17:00:00.000Z"
        body["services"]=[1]

        when: 'I try update a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/appointments/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().local == "editado"
    }
    void "test update failed appointments"() {
        def body=[:]
        given: 'a changes for the appointment'
        body["id"]=200
        body["local"]="noExiste"
        body["dayHour"]= "2019-10-07T20:00:00.000Z"
        body["services"]=[1]
        when: 'I try update a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/appointments/200",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add failed appointment"() {
        def body = [:]
        given: 'a new appointment'
        body["local"] = "turnNew"
        body["client"] = 1
        body["status"] = "OPEN"
        body["services"] = [1]
        when: 'I try add a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments", body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "dayHour cant be null"
    }

    void "test search appointments"() {
        when: 'I try search a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/appointments/search?professional=1"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].local == "San Miguel"
    }

    void "test search appointments with no results"() {
        when: 'I try search a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/appointments/search?professional=2"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 0

    }

}
