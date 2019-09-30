package lookapp.backend


import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import io.micronaut.http.client.exceptions.HttpClientResponseException

@Integration
class LoginSpec  extends Specification  {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test login"() {
        def body=[:]
        given: 'a valid username and password'
        body["username"]="pedro"
        body["password"]="pedro"

        when: 'I try login'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/login",body), Map)

        then: 'The result is ...'
        response.status().code == 200
   }

    void "test invalid username"() {
        def body=[:]
        given: 'a invalid username and password'
        body["username"]="test"
        body["password"]="test"

        when: 'I try login'
        response = client.toBlocking().exchange(HttpRequest.POST("/login",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "invalid username or password"
    }

    void "test invalid password"() {
        def body=[:]
        given: 'a invalid username and password'
        body["username"]="pedro"
        body["password"]="pedro2"
        when: 'I try login'
        response = client.toBlocking().exchange(HttpRequest.POST("/login",body), Map)
        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "invalid username or password"
    }

    void "test without username"() {
        def body=[:]
        given: 'a invalid username and password'
        when: 'I try login'
        response = client.toBlocking().exchange(HttpRequest.POST("/login",body), Map)
        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "invalid username or password"
    }

    void "test without password"() {
        def body=[:]
        given: 'a invalid username and password'
        body["username"]="pedro"
        when: 'I try login'
        response = client.toBlocking().exchange(HttpRequest.POST("/login",body), Map)
        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "invalid username or password"
    }
}
