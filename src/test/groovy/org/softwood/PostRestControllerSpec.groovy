package org.softwood

import coffeeshopapp.BootStrap
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.util.slurpersupport.NodeChild
import groovy.xml.XmlUtil
import org.softwood.api.PostRestController
import spock.lang.Specification
import org.softwood.security.User
import org.softwood.security.UserProfile

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostRestController)
@Mock ([User, Post, UserProfile, Venue])
class PostRestControllerSpec extends Specification {

    def setupSpec() {
        defineBeans {
            //ensures springSecurityService bean is injected into user domain instances
            springSecurityService (SpringSecurityService)
        }
    }

    def setup() {
    }

    def cleanup() {
    }

    void "GET a list of posts as json using index() "() {
        given:"setup initial data"
            BootStrap.loadTestData()

        when: "i invoke index action"
            controller.index()

        then:"i get the expected posts as a json list "
            response.json*.description.sort() == [
                    "A first post",
                    "A second post",
                    "feeling so much better",
                    "just off for a coffee"
             ]
    }

    void "GET a list of posts as xml, using show()"() {
        given:"setup initial data"
            BootStrap.loadTestData()

        when: "i invoke the index action without an ID and response in xml"
            controller.index()
            def res = XmlUtil.serialize(new NodeChild (response.xml))
            println res//XmlUtil.serialize (response.xml)

        then:"i get the expected posts as a xml list "
            response.xml.post.description*.text().sort() == [
                    "A first post",
                    "A second post",
                    "feeling so much better",
                    "just off for a coffee"
            ]

    }
}
