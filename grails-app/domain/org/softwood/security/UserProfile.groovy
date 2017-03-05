package org.softwood.security

import org.softwood.security.User


class UserProfile implements Serializable {

    String fullname
    String nickname
    String emailAddress
    String imAddress
    String facebookId
    String googleId
    String microsoftId
    String timezone
    byte[] picture

    //bidirectional one-one cascade controlled by User
    static belongsTo = [user:User]

    static constraints = {
        fullname        blank:false
        nickname        nullable:true
        emailAddress    nullable:true, email:true
        imAddress       nullable:true, email:true
        facebookId      nullable:true, email:true
        googleId        nullable:true, email:true
        microsoftId     nullable:true, email:true
        timezone        nullable:true
        picture         nullable:true, size:0..2*1024*2024
    }
}
