package org.softwood

import java.time.LocalDate

class Venue implements Serializable {

    String name
    LocalDate dateCreated
    //LocalDate lastVisited  jsonview 1.1.5 cant handle LocalDateTime
    //LocalDate lastUpdated
    Date lastVisited
    Date lastUpdated
    GeoAddress location
    Collection<Post> posts

    static hasMany = [posts:Post]           //but doesn't really own thats with user
    static embedded =['location']

    static constraints = {
        lastVisited nullable:true
        location    nullable:true, unique:true
        posts       nullable:true
    }
    static mapping = {
        location cascade: "all-delete-orphan", lazy:false, unique:true  //eager fetch strategy
        posts    sorted: "desc", cascade:"save-update"

    }

    //moving to subclass removes the spurious table, but no location.id is generated and
    //tests still fail
    static class GeoAddress {

        String addressLine1
        String addressLine2
        String addressLine3
        String town
        String county
        String country = "UK"
        String postcode

        //adds addTo/removeFrom methods to venue
        static belongsTo = Venue

        static constraints = {
            addressLine1 nullable:true
            addressLine2 nullable:true
            addressLine3 nullable:true
            town         nullable:true
            county       nullable:true
            country      nullable:true
            postcode     nullable:true,
                    matches: /^([gG][iI][rR] {0,}0[aA]{2})|([a-pr-uwyzA-PR-UWYZ](([0-9](([0-9]|[a-hjkstuwA-HJKSTUW])?)?)|([a-hk-yA-HK-Y][0-9]([0-9]|[abehmnprvwxyABEHMNPRVWXY])?)) ?[0-9][abd-hjlnp-uw-zABD-HJLNP-UW-Z]{2})$/
        }
    }
}



