package org.softwood.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='name')
@ToString(includes='name', includeNames=true, includePackage=false)
class UserGroup implements Serializable {

	private static final long serialVersionUID = 1

	String name

	Set<Role> getAuthorities() {
		UserGroupToRole.findAllByUserGroup(this)*.role
	}

	static constraints = {
		name blank: false, unique: true
	}

	static mapping = {
		cache true
	}
}
