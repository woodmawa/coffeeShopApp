package org.softwood

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.softwood.security.UserProfile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(WillsUserService)
@Mock ([WillsUser, UserProfile, Post])
class WillsUserServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "get user by name "() {
        WillsUser user
        def res1
        def res2
        def res3

        given :"set of new user "
            WillsUser.withNewSession { session ->
                user = new WillsUser (username: 'testuser')
                user.save (flush:true, failOnError:true)
            }
            println "user stored in new session was ${WillsUser.list()}"
            assert user.id == 1
            assert user.username == 'testuser'
            assert WillsUser.count() == 1


        when: "get a user by a username string "
            WillsUser.withNewSession { session ->
                assert WillsUser.count() == 1
                user = WillsUser.get(1)
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
