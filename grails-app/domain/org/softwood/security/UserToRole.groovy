package org.softwood.security

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

import java.time.LocalDateTime

@ToString(cache=true, includeNames=true, includePackage=false)
class UserToRole implements Serializable {

	private static final long serialVersionUID = 1

	User user
	Role role
    LocalDateTime dateCreated

	@Override
	boolean equals(other) {
		if (other instanceof UserToRole) {
			other.userId == user?.id && other.roleId == role?.id
		}
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (user) builder.append(user.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static UserToRole get(long userId, long roleId) {
		criteriaFor(userId, roleId).get()
	}

	static boolean exists(long userId, long roleId) {
		criteriaFor(userId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long roleId) {
		UserToRole.where {
			user == User.load(userId) &&
			role == Role.load(roleId)
		}
	}

	static UserToRole create(User user, Role role, Boolean flush = false) {
		def instance = new UserToRole(user: user, role: role)
		instance.save(flush:flush)
		instance
	}

	static boolean remove(User u, Role r) {
		if (u != null && r != null) {
			UserToRole.where { user == u && role == r }.deleteAll()
		}
	}

	static int removeAll(User u) {
		u == null ? 0 : UserToRole.where { user == u }.deleteAll()
	}

	static int removeAll(Role r) {
		r == null ? 0 : UserToRole.where { role == r }.deleteAll()
	}

	static constraints = {
		role validator: { Role r, UserToRole ur ->
			if (ur.user?.id) {
				UserToRole.withNewSession {
					if (UserToRole.exists(ur.user.id, r.id)) {
						return ['userRole.exists']
					}
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'role']
		version false
		cache true

	}
}
