package org.softwood


import grails.test.mixin.integration.Integration
import grails.transaction.*
import spock.lang.*

@Integration
@Rollback
class WillsUserIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "create user"() {

        when: "we create a user and save them"
            WillsUser u = new WillsUser (username: "will")
            u.save (flush:true, failOnError:true)

        then:"check no errors"
            u.errors.errorCount  == 0
            u.id != 0
            WillsUser.get (u.id).username == "will"
    }

    void "check basic validations " () {
        given : "new user"
        WillsUser u = new WillsUser (username:"wil")  //too few chars

        when: "we validate "
        u.validate()

        then: " confirmed errors "
        u.hasErrors()
        u.errors.getFieldError ("username").code == "size.toosmall"
    }
    
    void "create two users with same name "() {
        when: "create two users with same username and save them "
        WillsUser u1 = new WillsUser (username:"will")
        u1.save (flush:true)
        WillsUser u2 = new WillsUser (username: "will")
        u2.save(flush:true)

        then: "second save will fail "
        u1.errors.errorCount == 0
        u1.id != null
        WillsUser.get (u1.id).username == "will"

        u2.hasErrors()
        u2.id == null
        u2.errors.getFieldError ("username").code == "unique"
        u2.errors.getFieldError ("username").rejectedValue == "will"


    }
}
