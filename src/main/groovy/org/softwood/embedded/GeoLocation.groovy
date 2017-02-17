package org.softwood.embedded

import org.softwood.Venue
import org.springframework.validation.annotation.Validated

/**
 * Created by will on 15/02/2017.
 */

@Validated
class GeoAddress {

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