package org.softwood.security

import grails.plugin.springsecurity.rest.RestTokenCreationEvent
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.userdetails.GrailsUser
import groovy.util.logging.Log4j
import groovy.util.logging.Slf4j
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent
import org.springframework.security.authentication.event.AuthenticationFailureDisabledEvent
import org.springframework.security.authentication.event.AuthenticationFailureExpiredEvent
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent
import org.springframework.security.authentication.event.AuthenticationFailureProviderNotFoundEvent
import org.springframework.security.authentication.event.AuthenticationFailureProxyUntrustedEvent
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import grails.plugin.springsecurity.rest.token.AccessToken

/**
 * Created by willw on 03/03/2017.
 */
@Slf4j
class CoffeeShopAppSecurityEventListener implements ApplicationListener {

    void onApplicationEvent (ApplicationEvent  event ) {

        //just process the security events
        switch (event.getClass()) {
            case RestTokenCreationEvent:
                println "/api login generated token "
                log.debug "restTokenCreated    " + event.asType(RestTokenCreationEvent).authentication
                break
            case AuthenticationSuccessEvent:
                log.debug "authentication success " + event.asType(AuthenticationSuccessEvent).authentication
                GrailsUser principal
                if (event.asType(AuthenticationSuccessEvent).source.getClass() == AccessToken) {
                    AccessToken token = event.asType(AuthenticationSuccessEvent).source
                    principal = token.principal
                } else {
                    UsernamePasswordAuthenticationToken source = event.asType(AuthenticationSuccessEvent).source
                    principal = source.principal
                }
                //AccessToken
                println "authenticated > " + principal
                break
            case AuthenticationFailureBadCredentialsEvent:
                log.debug "authentication fail bad credentials  " + event.asType(AuthenticationFailureBadCredentialsEvent).authentication
                break
            case AuthenticationFailureCredentialsExpiredEvent:
                log.debug "authentication fail credentials expired  " + event.asType(AuthenticationFailureCredentialsExpiredEvent).authentication
                break
            case AuthenticationFailureDisabledEvent:
                log.debug "authentication fail account disabled  " + event.asType(AuthenticationFailureDisabledEvent).authentication
                break
            case AuthenticationFailureExpiredEvent:
                log.debug "authentication fail account expired  " + event.asType(AuthenticationFailureExpiredEvent).authentication
                break
            case AuthenticationFailureLockedEvent:
                log.debug "authentication fail account locked  " + event.asType(AuthenticationFailureLockedEvent).authentication
                break
            case AuthenticationFailureProviderNotFoundEvent:
                log.debug "no authentication provider found   " + event.asType(AuthenticationFailureProviderNotFoundEvent).authentication
                break
            case AuthenticationFailureProxyUntrustedEvent:
                log.debug "authentication untrusted proxy used   " + event.asType(AuthenticationFailureProxyUntrustedEvent).authentication
                break
            case AuthenticationFailureServiceExceptionEvent:
                log.debug "authentication service failure    " + event.asType(AuthenticationFailureServiceExceptionEvent).authentication
                break

            default:
                //ignore other events
                break
        }
   }


}
