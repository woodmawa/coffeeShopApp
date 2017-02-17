package org.softwood

import grails.transaction.Transactional
import groovy.util.logging.Log

import java.util.concurrent.ConcurrentHashMap

@Transactional
@Log
class UserPostsService {

    def getPostsForUser(username) {
        User u = User.findByUsernameLike (username)
        assert u

        return u.posts

    }

    //single comments entry using tag, and value
    def createUserPost (User user, tag, value, StarRating starRating = StarRating.None, String description = null) {
        assert tag
        assert value
        if (!user.id) {
            log.debug "user $user has not been saved yet"
            return null
        }
        if (!user.isAttached()) {
            user.attach()       //reattach to session
        }
        def comments = new ConcurrentHashMap ()
        comments.put (tag, value)  //[tag, value]
        Post newPost = new Post (comments: comments, rating:new Rating (stars:starRating))
        user.addToPosts (newPost)
        user.save()
        if (user.hasErrors()){
            println "couldnt save post on user ${newPost.errors}"
            return null
        } else
            return newPost    }

    //comments map may have 0..m comment entries
    def createUserPost (User user, Map comments, StarRating starRating = StarRating.None, String description = null) {
        assert comments instanceof Map
        if (!user.id) {
            log.debug "user $user has not been saved yet"
            return null
        }
        if (!user.isAttached()) {
            user.attach()       //reattach to session
        }
        Post newPost = new Post (comments: comments, rating:new Rating (stars:starRating))
        assert newPost.comments
        user.addToPosts (newPost)
        user.save()
        if (user.hasErrors()){
            println "couldnt save post on user ${user.errors}"
            return null
        } else
            return newPost
    }

    def createUserPost (User user, Post post) {
        if (!user.id) {
            log.debug "user $user has not been saved yet"
            return null
        }
        if (!user.isAttached()) {
            user.attach()       //reattach to session
        }
        assert !post?.id
        if (!post.comments)
            post.comments = new HashMap()
        user.addToPosts (post)
        user.save()
        return post
    }

    def updatePost (Post post, Map comments,  String description = null ) {
        if (!post.isAttached()) {
            post.attach()
        }
        post.comments << comments
        post.description = description
        post.save()
        assert !post.hasErrors()
        return post
    }

    def updatePostRating (Post post, StarRating starRating) {

        if (!post.isAttached()) {
            post.attach()
        }
        assert post.user
        post.rating.stars = starRating
        post.save()
        assert !post.hasErrors()
        return post
    }

    def updatePostDescriptiong (Post post, String description) {

        if (!post.isAttached()) {
            post.attach()
        }
        assert post.user
        post.description = description
        post.save()
        assert !post.hasErrors()
        return post
    }
}
