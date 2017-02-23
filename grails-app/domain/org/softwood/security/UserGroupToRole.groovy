package org.softwood.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString
import org.apache.commons.lang.builder.HashCodeBuilder

import java.time.LocalDateTime

/**
 * Created by will on 21/02/2017.
 */

@ToString(cache=true, includeNames=true, includePackage=false)
class UserGroupToRole implements Serializable {

    private static final long serialVersionUID = 1

    UserGroup userGroup
    Role role
    LocalDateTime dateCreated

    static constraints = {
        userGroup blank: false /*, validator: { UserGroup ug, UserToUserGroup ug2r ->
            if (r.role?.id) {
                UserToUserGroup.withNewSession {
                    if (UserToUserGroup.exists(ug.id, ug2r.role.id)) {
                        return ['role.exists']
                    }
                }
            }
        }*/

        role blank: false
    }

    @Override
    boolean equals(other) {
        if (other instanceof UserGroupToRole) {
            other.userGroup?.id == userGroup?.id && other.role?.id == role?.id
        }
    }

    @Override
    int hashCode() {
        def builder = new HashCodeBuilder()
        if (userGroup) builder.append(userGroup.id)
        if (role) builder.append(role.id)
        builder.toHashCode()
    }

    static UserGroupToRole get(long userGroupId, long roleId) {
        criteriaFor(userGroupId, roleId).get()
    }

    static boolean exists(long userGroupId, long roleId) {
        criteriaFor(userGroupId, roleId).count()
    }

    private static DetachedCriteria criteriaFor(long userGroupId, long roleId) {
        UserGroupToRole.where {
            userGroup == UserGroup.load(userGroupId) &&
                    role == Role.load(roleId)
        }
    }

    static UserGroupToRole create(UserGroup group, Role role, boolean flush = true) {
        def ug2r = new UserGroupToRole(userGroup: group, role: role)
        ug2r.save(flush:flush)
        ug2r
    }

    static boolean remove(UserGroup ug, Role r) {
        if (ug != null && r != null) {
            UserGroupToRole.where { userGroup == ug && role == r }.deleteAll()
        }
    }

    static int removeAll(UserGroup ug) {
        ug == null ? 0 : UserGroupToRole.where { userGroup == ug }.deleteAll()
    }

    static int removeAll(Role r) {
        r == null ? 0 : UserGroupToRole.where { role == r }.deleteAll()
    }

    static mapping = {
        id composite: ['userGroup','role']
        version false
    }
}
