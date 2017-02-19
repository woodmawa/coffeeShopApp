package coffeeshopapp

import org.softwood.*

class BootStrap {

    def init = { servletContext ->
        environments{
            development{
                if (Post.list().size() == 0 )
                    loadTestData()
            }
            test {

            }
            production{

            }
        }
    }
    def destroy = {
    }

    def loadTestData() {

        WillsUserProfile up = new WillsUserProfile (fullname:"will woodman",
                nickname:"wiggy",
                emailAddress:"will.woodman@btinternet.com")
        WillsUser u = new WillsUser (username:"bootstrapWill", profile:up)
        if (u.validate()) {
            u.save(flush:true, failOnError:true)
            println "saved user u, with username Will"
        }
        Venue v = new Venue (name:"home",
                location: new Venue.GeoAddress (addressLine1:"10 South Close",
                country: "UK",
                county: "Suffolk",
                postcode: "IP4 2TH")
        )
        if (v.validate()) {
            v.save (flush:true, failOnError:true)
            println "saved venue v, as home"
        }
        Post p1 = new Post(comment: [food: "bought coffee and cake "], venue:v)
        Post p2 = new Post(comment: [dinner: "bought wine and dinner"], venue:v)
        Post p3 = new Post(comment: [view: "spectacular view of lake and sunset"], venue:v, rating:new Rating (stars: StarRating.FiveStar))
        Post p4 = new Post(comment: [random: "have a nice day"], rating:new Rating (stars: StarRating.OneStar))
        u.addToPosts (p1)
        u.addToPosts (p2)
        u.addToPosts (p3)
        u.addToPosts (p4)
        u.save (flush:true, failOnError:true)

    }
}
