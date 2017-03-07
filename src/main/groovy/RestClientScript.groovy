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

        println "apiKey: $apiKey"
    }
    response.'404' = { resp ->
        println 'Not found'
    }
}

http = new groovyx.net.http.HTTPBuilder("http://localhost:8080")
def validateToken = [username:"$username", roles:["ROLE_ADMIN"], token_type: "$tokenType", access_token : "$apiKey" ]
println "validate using : $validateToken"
http.request (POST, JSON) { req ->
    uri.path = "/api/validate"
    headers.'Accept' = 'application/json'
    headers.'Content-Type' = 'application/json'
    headers.'Authorization' = apiKey
    //requestContentType = JSON

    body = validateToken

    response.success = { HttpResponseDecorator resp, LazyMap results ->
        assert resp.status == 200
        println "validate response : $resp.statusLine   "
        //println "length: ${resp.headers.'Content-Length'}"

        //println "validate: $results"
    }
    response.'401' =
    response.'404' = { resp ->
        println 'Not found'
    }
}