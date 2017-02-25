package org.softwood.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class UserUserGroupBroken implements Serializable {

	private static final long serialVersionUID = 1

	User user
	UserGroup userGroup

	@Override
	boolean equals(other) {
		if (other instanceof UserUserGroupBroken) {
			other.userId == user?.id && other.userGroupId == userGroup?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (userGroup) builder.append(userGroup.id)
		builder.toHashCode()
	}

	static UserUserGroupBroken get(long userId, long userGroupId) {
		criteriaFor(userId, userGroupId).get()
	}

	static boolean exists(long userId, long userGroupId) {
		criteriaFor(userId, userGroupId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long userGroupId) {
		UserUserGroupBroken.where {
			user == User.load(userId) &&
			userGroup == UserGroup.load(userGroupId)
		}
	}

	static UserUserGroupBroken create(User user, UserGroup userGroup) {
		def instance = new UserUserGroupBroken(user: user, userGroup: userGroup)
		instance.save()
		instance
	}

	static boolean remove(User u, UserGroup rg) {
		if (u != null && rg != null) {
			UserUserGroupBroken.where { user == u && userGroup == rg }.deleteAll()
		}
	}

	static int removeAll(User u) {
		u == null ? 0 : UserUserGroupBroken.where { user == u }.deleteAll()
	}

	static int removeAll(UserGroup rg) {
		rg == null ? 0 : UserUserGroupBroken.where { userGroup == rg }.deleteAll()
	}

	static constraints = {
		user validator: { User u, UserUserGroupBroken ug ->
			if (ug.userGroup?.id) {
				UserUserGroupBroken.withNewSession {
					if (UserUserGroupBroken.exists(u.id, ug.userGroup.id)) {
						return ['userGroup.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['userGroup', 'user']
		version false
	}
}
