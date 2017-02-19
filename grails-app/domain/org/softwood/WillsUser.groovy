package org.softwood


import java.time.LocalDate

class WillsUser implements Serializable {

    String username
    String password
    LocalDate dateCreated
    LocalDate lastUpdated
    Collection posts
    WillsUserProfile profile

    static hasOne =[profile:WillsUserProfile]
    static hasMany = [posts:Post]

    static constraints = {
        username blank: false, unique:true, size:4..20
        password nullable:true
        posts    nullable:true
        profile  nullable:true, unique:true
    }

    static mapping = {
        sort "username" //default sort on username
        //when querying posts via from Users define the sort oder for the posts Collection via relationship
        //name the collection, field to sort by on that entity, and the order
        posts sort: 'dateCreated', order:'desc'
    }

    boolean isTransient () {
        if (id == null)
            true
        else
            false
    }

    String toString() {
        def state = (isTransient()) ? "transient" : "persisted"
        "$username[$state]"
    }
}
