package org.softwood.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.softwood.Post

import java.time.LocalDate

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

	//added stuff
	//LocalDate dateCreated  json views 1.1.5 cant handle localDateTime
	//LocalDate lastUpdated
	Date dateCreated
	Date lastUpdated
	Collection posts
	UserProfile profile

	static hasOne =[profile:UserProfile]
	static hasMany = [posts:Post]


	//just stick with standard plugin model - its safer
	Set<UserGroup> getAuthorities() {
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

		//added stuff
		posts    nullable:true
		profile  nullable:true, unique:true

	}

	static mapping = {
		password column: '`password`'

		//added stuff
		sort "username" //default sort on username
		//when querying posts via from Users define the sort oder for the posts Collection via relationship
		//name the collection, field to sort by on that entity, and the order
		posts sort: 'dateCreated', order:'desc'

	}

	boolean isTransient () {
		if (id == null)
			true
		else
			false
	}

	String toString() {
		def state = (isTransient()) ? "transient" : "persisted"
		"$username[$state]"
	}
}
