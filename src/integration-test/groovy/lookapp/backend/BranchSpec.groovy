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
class BranchSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list branches"() {
        def body=[:]

        when: 'I try get a list of branches'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/branches"), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 2
        response.body()[0].name == "Hair&Head_Pacheco"
    }

    void "test get a branch"() {
        def body=[:]

        when: 'I try get a branch'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.GET("/branches/1"), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Hair&Head_Pacheco"
    }

    void "test update branch"() {
        def body=[:]
        given: 'a changes for the branch'
        body["id"]=1
        body["name"]="Hair&Head_Pacheco_nueva_sucursal"

        when: 'I try update a branch'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/branches/1",body), Map)

        then: 'The result is ...'
        response.status().code == 200
        response.body().name == "Hair&Head_Pacheco_nueva_sucursal"
    }

    void "test update failed branch"() {
        def body=[:]
        given: 'a changes for the branch'
        body["id"]=200
        body["name"]="Hair&Head_Pacheco"

        when: 'I try update a branch'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.PUT("/branches/200",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Not Found"
    }

    void "test add branch"() {
        def body=[:]
        given: 'a new branch'
        body["branch"]=1
        body["name"]="Hair&Head_Microcentro"
        body["street_name"] = "munoz"
        body["street_number"] = "2016"
        body["location"] = "San Miguel"
        body["status"] = "ACTIVE"

        when: 'I try to add a branch'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/branches",body), Map)

        then: 'The result is ...'
        response.status().code == 201
        response.body().name == "Hair&Head_Microcentro"
    }

     void "test add failed branch"() {
        def body=[:]
        given: 'a empty branch'
        body["branch"]=1
        body["street_name"] = "munoz"
        body["street_number"] = "2016"
        body["location"] = "San Miguel"
        body["status"] = "ACTIVE"

        when: 'I try add a branch'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/branches",body), Map)

        then: 'The result is ...'
        final HttpClientResponseException exception = thrown()
        exception.message == "Property [name] of class [class lookapp.backend.Branch] cannot be null"
    }

}
