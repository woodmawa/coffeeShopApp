package org.softwood

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.softwood.security.UserProfile
import spock.lang.Specification
import org.softwood.security.*
import org.softwood.Post

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserService)
@Mock ([User, UserProfile, Post])
class UserServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "get user by name "() {
        User user
        def res1
        def res2
        def res3

        given :"set of new user "
            User.withNewSession { session ->
                user = new User (username: 'testuser', password:"test")
                user.save (flush:true, failOnError:true)
            }
            println "user stored in new session was ${WillsUser.list()}"
            assert user.id == 1
            assert user.username == 'testuser'
            assert User.count() == 1


        when: "get a user by a username string "
            User.withNewSession { session ->
                assert User.count() == 1
                user = User.get(1)
                println "did get for user in new session found $user"
                res1 = service.getUserByName('test')  //defaults to non exact match
                assert res1
                res2 = service.getUserByName('test', true)  //set to exact match
                assert !res2
                res3 = service.getUserByName('testuser', true)  //set to exact match
                assert res3
           }

        then: "check result ok "
            user.username == 'testuser'
            res1.username == 'testuser'
            res3.username == 'testuser'
    }
}
