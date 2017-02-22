package coffeeshopapp

import org.softwood.*
import org.softwood.security.Role
import org.softwood.security.User
import org.softwood.security.UserGroup
import org.softwood.security.UserGroupToRole
import org.softwood.security.UserToUserGroup

class BootStrap {

    def init = { servletContext ->
        if (Role.count() == 0 ) {
            loadSecurityUserAndRoles()
        }

        environments{
            development{
                /*if (Post.list().size() == 0 )
                    loadTestData()*/
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
        //plugin requires ROLE_ prefix see section 4.2/p18

        Role adminRole = new Role(authority: 'ROLE_ADMIN').save(failOnError:true)
        Role userRole = new Role(authority: 'ROLE_USER').save(failOnError:true)
        UserGroup adminGroup = new UserGroup (name:"GROUP_ADMIN").save(failOnError:true)

        def testUser = new User(username: 'will', password: 'password').save(failOnError:true)

        /*UserToUserGroup uug = new UserToUserGroup()
        uug.user = testUser
        uug.group = adminGroup
        UserToUserGroup saved = uug.save(flush:true)
        UserToUserGroup.create (testUser, adminGroup) */


        UserGroupToRole sgr = UserGroupToRole.create(adminGroup, adminRole)
        sgr = UserGroupToRole.create(adminGroup, userRole)

        assert UserGroupToRole.count() == 2

        def auth2 = adminGroup.getAuthorities()
        println "adminGroup authorities returned $auth2 "

        UserToUserGroup su2g = UserToUserGroup.create (testUser, adminGroup, true)

        def auth = testUser.getAuthorities()
        assert auth.collect{it.authority}.sort() == ['ROLE_ADMIN', 'ROLE_USER']

        def groups = testUser.getUserGroups()
        assert groups.collect{it.name}.sort() == ['GROUP_ADMIN']

        assert UserGroup.count() == 1
        assert User.count() == 1
        assert Role.count() == 2
        assert UserToUserGroup.count() == 1
        assert UserGroupToRole.count() == 2

    }

    def loadTestData() {



        WillsUserProfile up = new WillsUserProfile (fullname:"will woodman",
                nickname:"wiggy",
                emailAddress:"will.woodman@btinternet.com")
        WillsUser u = new WillsUser (username:"bootstrapWill", profile:up)
        if (u.validate()) {
            u.save(flush:true, failOnError:true)
            println "saved user u, with username ${u.username}"
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
