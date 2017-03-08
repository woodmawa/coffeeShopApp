package coffeeshopapp

import org.softwood.*
import org.softwood.security.Role
import org.softwood.security.User
import org.softwood.security.UserGroup
import org.softwood.security.UserGroupToRole

import org.softwood.security.UserToUserGroup
import org.softwood.security.UserProfile

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
        Role xtraRole = new Role(authority: 'ROLE_XTRA').save(failOnError:true)
        UserGroup adminGroup = new UserGroup (name:"GROUP_ADMIN").save(failOnError:true)
        UserGroup userGroup = new UserGroup (name:"GROUP_USERS").save(failOnError:true)

        User userWill = new User(username: 'will', password: 'password').save(failOnError:true)
        User userMaz = new User(username: 'maz', password: 'password').save(failOnError:true)
        User userMeg = new User(username: 'meg', password: 'password').save(failOnError:true)

        //give adminGroup admin and user roles
        UserGroupToRole sugr = UserGroupToRole.create(adminGroup, adminRole, true)
        sugr = UserGroupToRole.create(adminGroup, userRole, true)

        sugr = UserGroupToRole.create(userGroup, userRole, true)

        assert UserGroupToRole.count() == 3

        def auth2 = adminGroup.getAuthorities()
        println "adminGroup authorities returned $auth2 "

        //assign test user to adminGroup, and maz+meg to user group, inherit all group roles
        UserToUserGroup su2ug = UserToUserGroup.create (userWill, adminGroup, true)
        su2ug = UserToUserGroup.create (userMaz, userGroup, true)
        su2ug = UserToUserGroup.create (userMeg, userGroup, true)

        def auth = userWill.getAuthorities()
        println "userWill (group) authorities returned $auth "

        def mazAuth = userMaz.authorities
        def megAuth = userMeg.authorities
        println "user authorities returned maz: '$mazAuth', and meg: '$megAuth' "

        Post post = new Post (comments : [view: 'lovely'], user: userWill /*, rating: new Rating(stars: StarRating.FiveStar)*/)
        userWill.addToPosts (post)

        post.save (flush:true)
        if (post.hasErrors())
            println "post save failed with errors $post.errors"

        assert UserGroup.count() == 2
        assert User.count() == 3
        assert Role.count() == 3
        assert UserToUserGroup.count() == 3
        assert UserGroupToRole.count() == 3

    }

    static def loadTestData() {



        UserProfile up = new UserProfile (fullname:"will woodman",
                nickname:"wiggy",
                emailAddress:"will.woodman@btinternet.com")
        User u = new User (username:"bootstrapWill", password: "password", profile:up)
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
        Post p1 = new Post(comment: [food: "bought coffee and cake "], venue:v, description : "A first post" )
        Post p2 = new Post(comment: [dinner: "bought wine and dinner"], venue:v, description : "A second post")
        Post p3 = new Post(comment: [view: "spectacular view of lake and sunset"], venue:v, rating:new Rating (stars: StarRating.FiveStar), description : "just off for a coffee")
        Post p4 = new Post(comment: [random: "have a nice day"], rating:new Rating (stars: StarRating.OneStar), description : "feeling so much better")
        u.addToPosts (p1)
        u.addToPosts (p2)
        u.addToPosts (p3)
        u.addToPosts (p4)
        u.save (flush:true, failOnError:true)


    }
}
