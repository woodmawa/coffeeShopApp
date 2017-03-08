package org.softwood


import grails.test.mixin.integration.Integration
import grails.transaction.*
import grails.util.GrailsWebMockUtil
import spock.lang.*

@Integration
@Rollback
class VenueIntegrationSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {

        when : "we create a new venue and save it "
            Venue v = new Venue (name:"bistro")
            def result = v.save(flush:true, failOnError:true)

        then:"check we have a id "
            result  != null
            v.errors.errorCount == 0
            v.id != null
            Venue.get (v.id).name == "bistro"

    }

    //original test -  this fails, have to explicitly delete loc to make it work
    void "test venue with an address" () {
        when: "create a venue and an address using transitive save on embedded "
            Venue.GeoAddress address = new Venue.GeoAddress (addressLine1: "myhouse", town: "Ipswich", county: "suffolk", postcode : "IP4 2TH")

            //address.save()
            Venue v = new Venue (name: "bistro", location: address)
            def result = v.save(flush:true)

        then: "retrieve venue and check its location loaded eagerly "
            Venue lookupVenue = Venue.get(v.id)
            Venue.GeoAddress loc = lookupVenue.location
            loc.postcode == "IP4 2TH"
            loc.town == "Ipswich"

        when: " we delete the venue, it deletes the embedded location (Address)"
            //loc.delete(flush:true)  //needs this to work, why?
            //v.location = null    //does this work
            v.delete (flush:true)
            if (v.hasErrors())
                println "errors: $v.errors"

            Venue.GeoAddress lookupLoc = Venue.GeoAddress.get (loc.id)

        then: "address should disppear"
            Venue.get (v.id) == null
            Venue.GeoAddress.findAll().size() == 0
            lookupLoc == null
    }


    //new test - reuse embedded  entity - works
    void "test with GeoLocation" () {
        when: ""

        Venue.GeoAddress a = new Venue.GeoAddress(town:"ipswich")
        //a.save() //causes test to fail
        Venue v = new Venue (name: "bistro", location: a)
        assert v.save(flush:true)

        Venue lookupVenue = Venue.get(v.id)

        Venue.GeoAddress ta = lookupVenue.location
        assert ta.town == "ipswich"
        def locList = Venue.GeoAddress.get(1)
        def tloc = locList[0]

        //try delete
        v.delete (flush:true)

        then : " retrieve index"
        Venue.GeoAddress.findAll().size() == 0
    }
}
