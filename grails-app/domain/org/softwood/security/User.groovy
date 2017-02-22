package org.softwood.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

	String username
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

    //TODO - should this be transative to roles via groups ?
	Set<Role> getAuthorities() {
		//UserUserGroupBroken.findAllByUser(this)*.userGroup
        Set<UserGroup> groups = UserToUserGroup.findAllByUser(this)*.group
        groups.collect{it.getAuthorities() }
	}

    Set<UserGroup> getUserGroups() {
        UserToUserGroup.findAllByUser(this)*.group
    }

    def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, password: true
		username blank: false, unique: true
	}

	static mapping = {
		password column: '`password`'
	}
}
