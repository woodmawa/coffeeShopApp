package org.softwood

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserPostsService)
@Mock ([User,Post, Rating])
class UserPostsServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "get posts for user with username X"() {
        given:"create user - then test the service "
            User u = new User (username:'testuser')
            assert u.save(flush:true)
            u.posts = []       //have to assign a collection here as starts null
            assert u.posts.size() == 0

            Post p1 = new Post (comments: [howdi: "how are you"])
            Post p2 = new Post (comments: [and: "hope you are well"])

            u.addToPosts (p1)
            u.addToPosts (p2)

        when: "inject and call service "
            Post[] posts = service.getPostsForUser ('testuser')

        then : "get the posts we "
            posts.size() == 2
            posts[0].comments['howdi'] == "how are you"    //read from cache as written not db sorted

    }

    void "create new user post"() {
        given : "a new user"
            User u = new User (username:'testuser')
            assert u.save(flush:true)
            u.posts = []       //have to assign a collection here as starts null
            assert u.posts.size() == 0

        when: "we call service to create a post "
            Post p = service.createUserPost (u, 'view', 'looks great', StarRating.OneStar)
            assert p
            assert !p.rating.hasErrors()
        then:"check post looks correct"
            p.id
            Post.get (p.id).comments.view == 'looks great'
            
    }

    void "create new user post and update it"() {
        given : "a new user, and initial post"
        Post p1
        Post p2
        User u
        User.withNewSession { session ->
            u = new User (username:'testuser')
            assert u.save(flush:true)
            u.posts = []       //have to assign a collection here as starts null
            assert u.posts.size() == 0
            p1 = service.createUserPost (u, 'view', 'looks great', StarRating.None)
            assert p1
            assert !p1.hasErrors()
            assert !p1.rating.hasErrors()
            assert p1.comments
            assert u.posts.size() == 1
            //create second post
            p2 = service.createUserPost (u, ['top': 'second post'], StarRating.OneStar)
            assert p2
            assert !p2.hasErrors()
            assert !p2.rating.hasErrors()
            assert p2.comments
            assert u.posts.size() == 2
        }

        when: "we then try and update the post "
        Post upd
        Post rev
        Post.withNewSession { session ->
            Map comments = new HashMap()
            comments.put ('view', 'looks even better')
            String descrip = "updated post"
            upd = service.updatePost (p1, comments, descrip )
            assert upd
            assert !upd.hasErrors()
        }

        rev = Post.get (upd.id)

        then: "confirm updated ok"
        upd.id
        upd.user.username == u.username
        rev.comments.view == 'looks even better'
        rev.description == "updated post"
        Post.count() == 2

    }

    void "create new user post and update post rating "() {
        given : "a new user, and initial post"
        Post p
        User u
        User.withNewSession { session ->
            u = new User (username:'testuser')
            assert u.save(flush:true)
            u.posts = []       //have to assign a collection here as starts null
            assert u.posts.size() == 0
            p = service.createUserPost (u, 'view', 'looks great', StarRating.None)
            assert p
            assert !p.rating.hasErrors()
            assert p.comments
        }

        when: "we then try and update the post "
        Post upd
        Post rev
        Post.withNewSession { session ->
            upd =service.updatePostRating (p, StarRating.FiveStar)
            assert upd
            assert !upd.hasErrors()
        }

        rev = Post.get (upd.id)

        then: "confirm updated ok"
        upd.id
        upd.user.username == u.username
        rev.rating.stars == StarRating.FiveStar

    }
}
