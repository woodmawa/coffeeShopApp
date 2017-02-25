package org.softwood.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class UserGroupRoleBroken implements Serializable {

	private static final long serialVersionUID = 1

	UserGroup userGroup
	Role role

	@Override
	boolean equals(other) {
		if (other instanceof UserGroupRoleBroken) {
			other.roleId == role?.id && other.userGroupId == userGroup?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (userGroup) builder.append(userGroup.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static UserGroupRoleBroken get(long userGroupId, long roleId) {
		criteriaFor(userGroupId, roleId).get()
	}

	static boolean exists(long userGroupId, long roleId) {
		criteriaFor(userGroupId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long userGroupId, long roleId) {
		UserGroupRoleBroken.where {
			userGroup == UserGroup.load(userGroupId) &&
			role == Role.load(roleId)
		}
	}

	static UserGroupRoleBroken create(UserGroup userGroup, Role role) {
		def instance = new UserGroupRoleBroken(userGroup: userGroup, role: role)
		instance.save()
		instance
	}

	static boolean remove(UserGroup rg, Role r) {
		if (rg != null && r != null) {
			UserGroupRoleBroken.where { userGroup == rg && role == r }.deleteAll()
		}
	}

	static int removeAll(Role r) {
		r == null ? 0 : UserGroupRoleBroken.where { role == r }.deleteAll()
	}

	static int removeAll(UserGroup rg) {
		rg == null ? 0 : UserGroupRoleBroken.where { userGroup == rg }.deleteAll()
	}

	static constraints = {
		role validator: { Role r, UserGroupRoleBroken rg ->
			if (rg.userGroup?.id) {
				UserGroupRoleBroken.withNewSession {
					if (UserGroupRoleBroken.exists(rg.userGroup.id, r.id)) {
						return ['roleGroup.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['userGroup', 'role']
		version false
	}
}
