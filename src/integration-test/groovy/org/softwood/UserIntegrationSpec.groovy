package org.softwood


import grails.test.mixin.integration.Integration
import grails.transaction.*
import org.softwood.security.User
import spock.lang.*

@Integration
@Rollback
class UserIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "create user"() {

        when: "we create a user and save them"
            User u = new User (username: "will", password:"password")
            u.save (flush:true, failOnError:true)

        then:"check no errors"
            u.errors.errorCount  == 0
            u.id != 0
            User.get (u.id).username == "will"
    }

    void "check basic validations " () {
        given : "new user"
        User u = new User (username:"wi", password:"password")  //too few chars, <3

        when: "we validate "
        u.validate()

        then: " confirmed errors "
        u.hasErrors()
        u.errors.getFieldError ("username").code == "size.toosmall"
    }
    
    void "create two users with same name "() {
        when: "create two users with same username and save them "
        User u1 = new User (username:"will",password:"password")
        u1.save (flush:true)
        User u2 = new User (username: "will", password:"password")
        u2.save(flush:true)

        then: "second save will fail "
        u1.errors.errorCount == 0
        u1.id != null
        User.get (u1.id).username == "will"

        u2.hasErrors()
        u2.id == null
        u2.errors.getFieldError ("username").code == "unique"
        u2.errors.getFieldError ("username").rejectedValue == "will"


    }
}
