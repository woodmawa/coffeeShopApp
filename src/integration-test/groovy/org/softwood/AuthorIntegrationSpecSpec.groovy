package org.softwood


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class AuthorIntegrationSpecSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        when:"create author "
            Author a = new Author (name:"will")
            def res = a.save(flush:true, failOnError:true)
            assert res

        then:" check resuslt "
        Author.get(1).name == "will"
    }
}
