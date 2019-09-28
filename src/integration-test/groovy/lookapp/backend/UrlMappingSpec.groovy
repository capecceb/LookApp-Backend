package lookapp.backend

import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Integration
class UrlMappingSpec extends Specification  {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "Test the homepage"() {
        when:"The home page is requested"
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/"), Map)

        then:"The response is correct"
        response.status == HttpStatus.OK
        response.header(HttpHeaders.CONTENT_TYPE) == 'application/json;charset=UTF-8'
        response.body().message == 'Welcome to Grails!'
    }
    void "Test not found"() {
        when:"The page is requested"
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/test"), Map)

        then:"The response is not found"
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

}
