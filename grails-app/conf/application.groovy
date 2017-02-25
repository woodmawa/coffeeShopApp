/**
 * Created by will on 13/02/2017.
 */

//added to avoid login screens for dbconsole.  if you add /** at end it works
grails.plugin.springsecurity.rejectIfNoRule = true
//grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.securityConfigType = "Annotation"

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.softwood.security.User'
grails.plugin.springsecurity.userLookup.authoritiesPropertyName = 'authorities'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.softwood.security.UserToUserGroup'
grails.plugin.springsecurity.authority.className = 'org.softwood.security.Role'
grails.plugin.springsecurity.authority.groupAuthorityNameField = 'authority'//'authorities'
grails.plugin.springsecurity.useRoleGroups = true
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/dbconsole/**',   access: ['permitAll']],    //added
	[pattern: '/console/**',     access: ['permitAll']],	//added
		[pattern: '/secureTest/secure',    access: ['ROLE_ADMIN']],
        [pattern: '/secureTest/index',     access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

