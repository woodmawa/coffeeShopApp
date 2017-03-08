package org.softwood


import grails.test.mixin.integration.Integration
import grails.transaction.*
import org.softwood.security.User
import spock.lang.*

@Integration
@Rollback
class PostIntegrationSpecSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test create user and some posts"() {
        given:"a user... "
            User u = new User (username:'will', password:"password")
            u.save(flush:true, failOnError:true)

        when: "create a post for user "
            Post p1 = new Post (comments: [food: "bought coffee and cake "])
            Post p2 = new Post (comments: [dinner: "bought wine and dinner"])
            Post p3 = new Post (comments: [view: "spectacular view of lake and sunset"], rating:new Rating(stars:StarRating.FiveStar))
            //p3.addToRating (new Rating(starRating:StarRating.FiveStar))
            u.addToPosts (p1)
            u.addToPosts (p2)
            u.addToPosts (p3)
            u.save(flush:true)
            if ( u.hasErrors())
                println "error saving posts on user u : ${u.errors}"
            List<Post> lookupPosts = User.get (u.id).posts
            lookupPosts.each {println "query via user > $it.dateCreated : $it.comments" }

            Post.findAll().each {println "query from Post > $it.dateCreated : $it.comments"}
            Post p = lookupPosts[0]

        then: " check post was added"
            !u.hasErrors ()
            lookupPosts.size() == 3
            u.posts[2].rating.stars == StarRating.FiveStar
            lookupPosts[1].comments.dinner == "bought wine and dinner"

    }

    void "test query"() {
        given:"a user and where query for users, and posts "
        User u

        when: "create a post for user "
        User.withNewSession { session ->
            u = new User(username: 'will', password: "password")
            u.save(flush: true, failOnError: true)
            Post p1 = new Post(comments: [food: "bought coffee and cake "])
            Post p2 = new Post(comments: [dinner: "bought wine and dinner"])
            Post p3 = new Post(comments: [view: "spectacular view of lake and sunset"])
            u.addToPosts(p1)
            u.addToPosts(p2)
            u.addToPosts(p3)
            u.save(flush: true)
            if (u.hasErrors())
                println "error saving posts on user u : ${u.errors}"


            def postList = User.get(u.id).posts
            postList.each { println "query via user.list using same session > $it.dateCreated : $it.comments" }

            Post.findAll().each { println "query via Post using same session > $it.dateCreated : $it.comments" }
            //println "first session with : $session"
        }
        //because still in same session it just returns the order from the 1st level cache - so force a
        //new session and let the DB do the sort
        def lookupPosts
        User.withNewSession{ session ->
            User uNew  = User.get(u.id)
            assert uNew
            lookupPosts = uNew.posts

            lookupPosts.each {println "query via user in new session > $it.dateCreated : $it.comments" }
            //println "second session with : $session"
        }



        then: " check post was added"
        !u.hasErrors ()
        lookupPosts.size() == 3
        lookupPosts[1].comments.dinner == "bought wine and dinner"

    }

    void "test where query from posts" (){
        given:"a user "
        User u

        when: "create a post for user "
        User.withNewSession { session ->
            u = new User(username: 'will2', password:"password")
            u.save(flush: true, failOnError: true)
            Post p1 = new Post(comments: [food: "bought coffee and cake "])
            Post p2 = new Post(comments: [dinner: "bought wine and dinner"])
            Post p3 = new Post(comments: [view: "spectacular view of lake and sunset"])
            Post p4 = new Post(comments: [view: "fabulous"],rating:new Rating(stars: StarRating.FourStar))
            Post p5 = new Post(comments: [notSoClever: "dud"],rating:new Rating(stars: StarRating.OneStar))

            u.addToPosts(p1)
            u.addToPosts(p2)
            u.addToPosts(p3)
            u.addToPosts(p4)
            u.addToPosts(p5)
            u.save(flush: true)
            if (u.hasErrors())
                println "error saving posts on user u : ${u.errors}"
        }

        def postList
        def ratedPostList
        //create new session and use whey query from post to user avoiding N+1
        User.withNewSession { session ->

            postList = Post.where {user.username == "will2"}.list()
            assert postList
            postList.each {println "query via Post with matching user in new session > $it.dateCreated : $it.comments" }

            //rating.stars.ordinal() >= StarRating.ThreeStar.ordinal()
            ratedPostList = Post.where {user.username == "will2" && (rating?.stars >= StarRating.None) }.list()
            assert ratedPostList
            ratedPostList.each {println "query via Post for rated posts > $it.dateCreated : $it.comments rating: ${it.rating?.stars}" }

        }

        then: " test sorted order worked "
        postList[0].comments.notSoClever == "dud"
        postList[4].comments.food == "bought coffee and cake "
        postList[0].user.username == "will2"
        ratedPostList.size() == 2
    }
}
