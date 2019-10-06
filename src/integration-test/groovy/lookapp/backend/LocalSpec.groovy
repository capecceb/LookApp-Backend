package lookapp.backend

import grails.testing.gorm.DomainUnitTest
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class LocalSpec extends Specification implements DomainUnitTest<Local> {



    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
