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
class ProfessionalSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list professionals"() {
        def body=[:]

        when: 'I try to get a list of professionals'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/professionals"),List)
        then: 'The result is...'
        response.status().code == 200
        response.body().size() == 2
        response.body()[0].name == "Pedro"
    }

    void "test get a professional"(){
        def body = [:]
        when: 'I try to get a Professional'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/professionals/1"), Map)
        then: 'The result is...'
        response.status().code == 200
        response.body().name == "Pedro"
    }

    void "test update professional"(){
        def body=[:]
        given: 'A changes for the professional'
        body["id"]=1
        body["name"] = "Alejandro"
        when: 'I try to update a professional'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/professionals/1",body),Map)
        then: 'The result is...'
        response.status().code == 200
        response.body().name == "Alejandro"
    }

    void "test update failed professional"(){
        def body=[:]
        given: 'A change for the professional'
        body["id"] = 2
        body["name"] = "Diego"
        when: 'I try to update a professional'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/professonals/200",body), Map)
        then: 'The result is...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add professional"(){
        def body=[:]
        given: 'A new professional'
        body["name"] = "Javier"
        body["status"] = "ACTIVE"
        when: 'I try to add a professional'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/professionals", body), Map)
        then: 'The result is...'
        response.status().code == 201
        response.body().name == "Javier"
     }

    void "test add failed professional"(){
        def body=[:]
        given: 'A empty professional'
        body["status"] = "ACTIVE"
        when: 'I try to add a professional'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/professionals", body), Map)
        then: 'The result is...'
        final HttpClientResponseException  exception = thrown()
        exception.message == "La propiedad [name] de la clase [class lookapp.backend.Professional] no puede ser nulo"
    }


}
