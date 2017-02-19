package org.softwood

import java.time.LocalDateTime

class Post implements Serializable {

    Map comments
    WillsUser user
    Venue venue
    String description
    Rating rating       //should this be an enum?

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static belongsTo = [user:WillsUser]
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
