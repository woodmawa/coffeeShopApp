package org.softwood

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured (closure = {
    assert request
    assert ctx
    println """details passed to rest controller >  
authentication name : ${authentication.name} 
is authenticed : ${isAuthenticated()} 
has any role from admin or user: ${hasAnyRole('ROLE_ADMIN','ROLE_USER')} 
has role admin : ${hasRole('ROLE_ADMIN')}
principal : $principal
"""
    true
} )

class PostRestController extends RestfulController {
    static  responseFormats = ["json", "xml"]

    //constructor - tells rest controller which domain class to scaffold
    PostRestController() {
        super (Post)
    }

    def index() {
        List<Post> postList = Post.list([fetch:[user:"join",rating:"join"]])
        println "postRest:returning index action with model :  $postList"
        respond postList, view:'index'  //trigger the templating engine
    }
}
