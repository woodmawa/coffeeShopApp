/**
 * Created by willw on 06/03/2017.
 */


import groovy.json.internal.LazyMap
import groovyx.net.http.HttpResponseDecorator

import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.PUT
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.TEXT
import static groovyx.net.http.ContentType.JSON


def endPoint = 'http://localhost:8080'



def http = new groovyx.net.http.HTTPBuilder("http://localhost:8080")

def login = [username:"will",password:"password"]  //willbe json encoded

def apiKey
def expireIn
def refreshKey
def roles
def tokenType
def username

http.request (POST, JSON) { req ->
    uri.path = "/api/login"
    headers.'Accept' = 'application/json'
    headers.'Content-Type' = 'application/json'
    requestContentType = JSON

    body = login
    response.success = { HttpResponseDecorator resp, LazyMap results ->
        assert resp.status == 200
        println "login response : $resp.statusLine   "
        println "length: ${resp.headers.'Content-Length'}"
        apiKey = results.access_token
        expiresIn = results.expires_in
        refreshKey = results.refresh_token
        roles = results.roles
        tokenType = results.token_type
        username = results.username

    }
    response.'404' = { resp ->
        println 'Not found'
    }
}


http = new groovyx.net.http.HTTPBuilder("http://localhost:8080")
def validateToken = [username:"$username", roles:["ROLE_ADMIN"], token_type: "$tokenType", access_token : "$apiKey" ]
println "validate using : $validateToken"
http.request (POST) { req ->
    uri.path = "/api/validate"
    headers.'Accept' = 'application/json'
    headers.'Content-Type' = 'application/json'
    headers.'Authorization' = "Bearer $apiKey"
    requestContentType = JSON

    body = validateToken

    response.success = { HttpResponseDecorator resp, LazyMap results ->
        assert resp.status == 200
        println "validate response : $resp.statusLine   "
        //println "length: ${resp.headers.'Content-Length'}"

        println "\tvalidate: $results"
    }
    response.'401' = { resp ->
        println 'unathorised access to resource /api/validate'
    }
    response.'403' = { resp ->
        println 'forbidden access to resource /api/validate'
    }
    response.'404' = { resp ->
        println 'Not found'
    }
}

println "retrived new apiKey: $apiKey"

http = new groovyx.net.http.HTTPBuilder("http://localhost:8080")
http.request (GET, JSON) { req ->
    uri.path = "/api/posts"
    headers.'Accept' = 'application/json'
    headers.'Content-Type' = 'application/json'
    headers.'Authorization' = "Bearer $apiKey"  //have to include Bearer then the key

    response.success = { HttpResponseDecorator resp, def results ->
        assert resp.status == 200
        println "GET api/posts : $resp.statusLine   "
        //println "length: ${resp.headers.'Content-Length'}"

        println "\t/api/posts: $results"
    }
    response.'401' = { resp ->
        println 'unathorised access to resource /api/posts'
    }
    response.'403' = { resp ->
        println 'forbidden access to resource /api/posts'
    }
    response.'404' = { resp ->
        println 'Not found'
    }
}