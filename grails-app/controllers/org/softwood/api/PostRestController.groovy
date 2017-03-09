package org.softwood.api

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import org.softwood.Post

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
    static namespace = "api"  //put views in grails-app/views/<ns>/<cntlr name>

    static  responseFormats = ["json", "xml"]

    //constructor - tells rest controller which domain class to scaffold
    PostRestController() {
        super (Post)
    }

    def index() {
        List<Post> postList = Post.list([fetch:[user:"join",rating:"join"]])
        respond postList, view:'index'  //trigger the templating engine
    }
}
