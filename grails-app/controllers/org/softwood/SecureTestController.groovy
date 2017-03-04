package org.softwood

import grails.plugin.springsecurity.annotation.Secured

class SecureTestController {

    def index() {
        render "hello Will you passed the permit_any"
    }

    @Secured ('ROLE_ADMIN')
    def secure () {
        render "hello Will you passed the ROLE_ADMIN"

    }

    @Secured("authentication.name == 'will'")
    def willsPage() {
        render "just got be will "
    }
}
