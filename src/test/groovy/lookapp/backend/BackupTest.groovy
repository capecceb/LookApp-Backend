package lookapp.backend


import grails.testing.mixin.integration.Integration
import grails.testing.spock.OnceBefore
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

@Integration
class BackupTest extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test export backup"() {
        def body=[:]

        when: 'I try get a backup'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/backup"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().get(0).size() == 15
    }

    void "test add import"() {
        given: 'a backup'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/backup"), List)
        def body=response.body().get(0)
        when: 'I try to import'
        response = client.toBlocking().exchange(HttpRequest.POST("/backup",body), Map)
        then: 'The result is ...'
        response.status().code == 200
    }
}
