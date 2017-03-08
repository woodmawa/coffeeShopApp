package org.softwood

import grails.rest.Resource
import org.softwood.security.User

import java.time.LocalDateTime

//adds default json and xml rest endpoints
//now using restController : @Resource (uri="/api/posts")
class Post implements Serializable {

    Map comments
    User user
    Venue venue
    String description
    Rating rating       //should this be an enum?

    //LocalDateTime dateCreated  //json vieww 1.1.5 cant cope with localDateTime
    //LocalDateTime lastUpdated
    Date dateCreated
    Date lastUpdated

    static belongsTo = [user:User]
    static hasOne = [rating:Rating]

    static constraints = {
        venue   nullable:true
        comments nullable:true
        description nullable:true

        rating  nullable:true, lazy:false
    }

    static mapping = {
        //set the sort order for Posts -  default using newest post first
        sort dateCreated :"desc"


    }
}
