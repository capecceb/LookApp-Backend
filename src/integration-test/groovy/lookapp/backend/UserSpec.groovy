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
class UserSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list users"() {
        def body=[:]

        when: 'I try get a list of users'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/users"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].username == "pedro"
    }

    void "test get a user"() {
        def body=[:]

        when: 'I try get a list of user'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/users/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().username == "pedro"
    }
    void "test update user"() {
        def body=[:]
        given: 'a changes for the user'
        body["id"]=1
        body["username"]="Juan"

        when: 'I try update a user'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/users/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().username == "Juan"
    }

    void "test update failed user"() {
        def body=[:]
        given: 'a changes for the user'
        body["id"]=2
        body["username"]="Juan"

        when: 'I try update a user'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/users/2",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add user"() {
        def body=[:]
        given: 'a new user'
        body["username"]="Juan"

        when: 'I try add a user'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/users",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().username == "Juan"
    }

    void "test add failed user"() {
        def body=[:]
        given: 'a empty user'

        when: 'I try add a user'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/users",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "La propiedad [username] de la clase [class lookapp.backend.User] no puede ser nulo"
    }
}
