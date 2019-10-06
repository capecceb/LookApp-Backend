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
class AppointmentSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

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
        response.body().size() == 3
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
    void "test update appointment"() {
        def body=[:]
        given: 'a changes for the appointment'
        body["id"]=1
        body["local"]="editado"

        when: 'I try update a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/appointments/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().local == "editado"
    }

    void "test update failed appointments"() {
        def body=[:]
        given: 'a changes for the appointment'
        body["id"]=5
        body["local"]="noExiste"

        when: 'I try update a appointment'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/appointments/200",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add appointment"() {
        def body=[:]
        Calendar cal = Calendar.getInstance()
        given: 'a new appointment'
        body["local"]="turnNew"
        body["client"]=1
        body["status"]="OPEN"
        body["dayHour"]=cal.getTime()

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/appointments",body), Map)

        then: 'The result is ...'
        response.status().code == 201
    }

    void "test add failed client"() {
        def body=[:]
        given: 'a empty client without surname'
        body["name"]="clienteNew"
        body["DNI"]="1111111"
        body["primaryPhone"]=2222
        body["secondPhone"]=33333

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/clients",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "La propiedad [surname] de la clase [class lookapp.backend.Client] no puede ser nulo"
    }
}
