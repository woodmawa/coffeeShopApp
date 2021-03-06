package org.softwood.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.apache.commons.lang.builder.HashCodeBuilder

import java.time.LocalDateTime

/**
 * Created by will on 21/02/2017.
 */

@ToString(cache=true, includeNames=true, includePackage=false)
class UserToUserGroup implements Serializable {

    private static final long serialVersionUID = 1

    User user
    UserGroup group
    LocalDateTime dateCreated

    static constraints = {
        user blank: false , validator: { User u, UserToUserGroup u2ug ->
            if (u2ug.group?.id) {
                UserToUserGroup.withNewSession {
                    if (UserToUserGroup.exists(u.id, u2ug.group.id)) {
                        return ['userGroup.exists']
                    }
                }
            }
        }
    }

    @Override
    boolean equals(other) {
        if (other instanceof UserToUserGroup) {
            other.user?.id == user?.id && other.group?.id == group?.id
        }
    }

    @Override
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (user) builder.append(user.id)
        if (group) builder.append(group.id)
        builder.toHashCode()
    }

    static UserToUserGroup get(long userId, long groupId) {
        criteriaFor(userId, groupId).get()
    }

    static boolean exists(long userId, long groupId) {
        criteriaFor(userId, groupId).count()
    }

    private static DetachedCriteria criteriaFor(long userId, long groupId) {
        UserToUserGroup.where {
            user == User.load(userId) &&
                    group == UserGroup.load(groupId)
        }
    }

    static UserToUserGroup create(User user, UserGroup group, boolean flush = false) {
        def u2ug = new UserToUserGroup(user: user, group: group)
        u2ug.save(flush:flush)
        u2ug
    }

    static boolean remove(User u, UserGroup g) {
        if (u != null && g != null) {
            UserToUserGroup.where { user == u && group == g }.deleteAll()
        }
    }

    static int removeAll(User u) {
        u == null ? 0 : UserToUserGroup.where { user == u }.deleteAll()
    }

    static int removeAll(UserGroup g) {
        g == null ? 0 : UserToUserGroup.where { group == g }.deleteAll()
    }

    static mapping = {
        id composite: ['user','group']
        version false
    }
}
