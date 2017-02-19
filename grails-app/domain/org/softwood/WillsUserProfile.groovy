package org.softwood


class WillsUserProfile implements Serializable {

    String fullname
    String nickname
    String emailAddress
    String imAddress
    String facebookId
    String googleId
    String microsoftId
    String timezone
    byte[] picture

    //bidirectional one-one cascade controlled by WillsUser
    static belongsTo = [user:WillsUser]

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
