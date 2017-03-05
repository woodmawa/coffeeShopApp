/**
 * Created by will on 13/02/2017.
 */

//added to avoid login screens for dbconsole.  if you add /** at end it works
grails.plugin.springsecurity.rejectIfNoRule = false//true
grails.plugin.springsecurity.fii.rejectPublicInvocations = false
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.useSecurityEventListener = true	//enable security events

//Rest
//grails.plugin.springsecurity.useBasicAuth = true
//grails.plugin.springsecurity.basic.realName = "coffeeShopApp"

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.softwood.security.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.softwood.security.UserToUserGroup'
grails.plugin.springsecurity.authority.className = 'org.softwood.security.Role'
grails.plugin.springsecurity.authority.groupAuthorityNameField = 'authorities'
grails.plugin.springsecurity.useRoleGroups = true
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/dbconsole/**',   access: ['permitAll']],    //added
	[pattern: '/console/**',     access: ['permitAll']],	//added
		[pattern: '/secureTest/secure',    		access: ['ROLE_ADMIN']],
        [pattern: '/secureTest/index',     		access: ['permitAll']],
		[pattern: '/secureTest/willsPage',     	access: ["authentication.name == 'will'"]],
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
    [pattern: '/api/**',         filters: 'JOINED_FILTERS,' +
            '-anonymousAuthenticationFilter,' +
            '-exceptionTranslationFilter,' +
            '-authenticationProcessingFilter,' +
            '-securityContextPersistenceFilter,' +
            '-rememberMeAuthenticationFilter'],

    [pattern: '/**',             filters: 'JOINED_FILTERS,' +
            '-basicAuthenticationFeature,' +
            '-basicExceptionTranslationFilter']
]

