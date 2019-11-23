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
class RolSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list roles"() {
        def body=[:]

        when: 'I try get a list of rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/roles"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
        response.body()[0].name == "Administrador"
    }

    void "test get rol"() {
        def body=[:]

        when: 'I try get a list of rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/roles/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Administrador"
    }
    void "test update rol"() {
        def body=[:]
        given: 'a changes for the rol'
        body["id"]=1
        body["name"]="Supervisor"

        when: 'I try update a rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/roles/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Supervisor"
    }

    void "test update failed role"() {
        def body=[:]
        given: 'a changes for the rol'
        body["id"]=2
        body["name"]="Supervisor"

        when: 'I try update a rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/roles/200",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add rol"() {
        def body=[:]
        given: 'a new rol'
        body["name"]="Supervisor"

        when: 'I try add a rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/roles",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().name == "Supervisor"
    }

    void "test add failed role"() {
        def body=[:]
        given: 'a empty rol'

        when: 'I try add a rol'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/roles",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Property [name] of class [class lookapp.backend.Rol] cannot be null"
    }
}
