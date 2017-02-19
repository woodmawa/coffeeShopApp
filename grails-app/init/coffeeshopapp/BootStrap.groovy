package coffeeshopapp

import org.softwood.*
import org.softwood.security.Role
import org.softwood.security.User

class BootStrap {

    def init = { servletContext ->
        if (Post.list().size() == 0 ) {

        }

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

    def loadSecurityUserAndRoles () {
        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()

        def testUser = new User(username: 'me', password: 'password').save()

        UserRole.create testUser, adminRole

        UserRole.withSession {
            it.flush()
            it.clear()
        }

        assert User.count() == 1
        assert Role.count() == 2
        assert UserRole.count() == 1

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
