package org.softwood

import grails.transaction.Transactional
import org.softwood.security.User
import org.softwood.security.UserProfile

@Transactional
class UserService {

    //TODO all security role/model stuff to do properly
    def getUserByName(String username, exactMatch=false) {
        User u
        if (!exactMatch)
            u = User.findByUsernameLike ("%$username%")
        else
            u = User.findByUsername (username)

        return u
    }


    def resetUserPassword (User user, password) {
        if (!user.isAttached) {
            user.attach()
        }

        if (user.validate()) {
            user.save (failOnError:true )
        }

        return user

    }

    def updateUserProfile (User user, Map bindingProfile){
        assert bindingProfile

        if (!user.isAttached) {
            user.attach()
        }

        if (!user.profile) {
            UserProfile profile = new UserProfile (bindingProfile)
            user.addToProfile(profile)
        }
        else {
            //use databinding to update the existing profile
            user.profile.properties = bindingProfile
        }
        if (!user.validate()) {
            user.save(failOnError:true)
        }

        return user
    }


}
