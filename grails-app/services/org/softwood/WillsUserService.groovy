package org.softwood

import grails.transaction.Transactional

@Transactional
class WillsUserService {

    //TODO all security role/model stuff to do properly
    def getUserByName(String username, exactMatch=false) {
        WillsUser u
        if (!exactMatch)
            u = WillsUser.findByUsernameLike ("%$username%")
        else
            u = WillsUser.findByUsername (username)

        return u
    }


    def resetUserPassword (WillsUser user, password) {
        if (!user.isAttached) {
            user.attach()
        }

        //TODO dont do this for real - redo when security model is applied
        user.password = password

        if (user.validate()) {
            user.save (failOnError:true )
        }

        return user

    }

    def updateUserProfile (WillsUser user, Map bindingProfile){
        assert bindingProfile

        if (!user.isAttached) {
            user.attach()
        }

        if (!user.profile) {
            WillsUserProfile profile = new WillsUserProfile (bindingProfile)
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
