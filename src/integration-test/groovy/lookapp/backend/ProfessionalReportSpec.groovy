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
class ProfessionalReportSpec extends Specification {

    @Shared
    @AutoCleanup
    HttpClient client

    static SimpleDateFormat sdfCrud = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @OnceBefore
    void init() {
        String baseUrl = "http://localhost:$serverPort"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    void "test list report"() {
        def body=[:]

        when: 'I try get a report'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/reportProfessional", body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 3
    }

    void "test list report, only branch 1"() {
        def body=[:]
        body["branchId"]=1

        when: 'I try get a report'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/reportProfessional", body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 2
    }

    void "test list report, only dates older than 08/11/2019"() {
        def body=[:]
        body["fromDate"]="2019-11-08T17:00:00.000Z"

        when: 'I try get a report'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/reportProfessional", body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 2
        response.body()[0].totalAmount == 400
    }

    void "test list report, only dates before than 08/11/2019"() {
        def body=[:]
        body["toDate"]="2019-11-08T17:00:00.000Z"

        when: 'I try get a report'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/reportProfessional", body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 2
        response.body()[0].totalAmount == 525
    }

    void "test list report, test all filters"() {
        def body=[:]

        body["branchId"]=2
        body["fromDate"]="2019-11-20T17:00:00.000Z"
        body["toDate"]="2019-11-28T17:00:00.000Z"

        when: 'I try get a report'
        HttpResponse<Map> response = client.toBlocking().exchange(HttpRequest.POST("/reportProfessional", body), List)

        then: 'The result is ...'
        response.status().code == 200
        response.body().size() == 1
        response.body()[0].totalAmount == 200
    }
}
