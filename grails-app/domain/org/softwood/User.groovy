package org.softwood


import java.time.LocalDate

class User {

    String username
    String password
    LocalDate dateCreated
    LocalDate lastUpdated
    Collection posts
    UserProfile profile

    static hasOne =[profile:UserProfile]
    static hasMany = [posts:Post]

    static constraints = {
        username blank: false, unique:true, size:4..20
        password nullable:true
        posts    nullable:true
        profile  nullable:true, unique:true
    }

    static mapping = {
        //when querying posts via from Users define the sort oder for the posts Collection via relationship
        //name the collection, field to sort by on that entity, and the order
        posts sort: 'dateCreated', order:'desc'
    }
}
