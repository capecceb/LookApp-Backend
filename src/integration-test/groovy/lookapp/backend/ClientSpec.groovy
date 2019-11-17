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
class ClientSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list clients"() {
        def body=[:]

        when: 'I try get a list of clients'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/clients"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        response.body()[0].name == "cliente1"
    }

    void "test get a clients"() {
        def body=[:]

        when: 'I try get a list of clients'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/clients/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "cliente1"
    }
    void "test update client"() {
        def body=[:]
        given: 'a changes for the client'
        body["id"]=1
        body["name"]="editado"

        when: 'I try update a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/clients/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "editado"
    }

    void "test update failed clients"() {
        def body=[:]
        given: 'a changes for the client'
        body["id"]=5
        body["name"]="noExiste"

        when: 'I try update a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/clients/5",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add client"() {
        def body=[:]
        given: 'a new client'
        body["name"]="clienteNew"
        body["DNI"]="1111111"
        body["lastName"]="clienteNewSur"
        body["primaryPhone"]=2222
        body["secondPhone"]=33333
        body["status"] = "VIP"
        body["email"]= "newClient@gmail.com"
        body["accountancy"] = 1

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/clients",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().name == "clienteNew"
    }

    void "test add failed client"() {
        def body=[:]
        given: 'a empty client without surname'
        body["name"]="clienteNew"
        body["DNI"]="1111111"
        body["primaryPhone"]=2222
        body["secondPhone"]=33333
        body["status"] = "NORMAL"
        body["email"]= "newClient@gmail.com"

        when: 'I try add a client'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/clients",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "La propiedad [lastName] de la clase [class lookapp.backend.Client] no puede ser nulo"
    }
}
