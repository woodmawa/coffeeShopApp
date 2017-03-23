package org.softwood.auto

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = false/*true*/)
class TestAutoUIController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond TestAutoUI.list(params), model:[testAutoUICount: TestAutoUI.count()]
    }

    def show(TestAutoUI testAutoUI) {
        respond testAutoUI
    }

    def create() {
        respond new TestAutoUI(params)
    }

    @Transactional
    def save(TestAutoUI testAutoUI) {
        if (testAutoUI == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testAutoUI.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testAutoUI.errors, view:'create'
            return
        }

        testAutoUI.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testAutoUI.label', default: 'TestAutoUI'), testAutoUI.id])
                redirect testAutoUI
            }
            '*' { respond testAutoUI, [status: CREATED] }
        }
    }

    def edit(TestAutoUI testAutoUI) {
        respond testAutoUI
    }

    @Transactional
    def update(TestAutoUI testAutoUI) {
        if (testAutoUI == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (testAutoUI.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond testAutoUI.errors, view:'edit'
            return
        }

        testAutoUI.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'testAutoUI.label', default: 'TestAutoUI'), testAutoUI.id])
                redirect testAutoUI
            }
            '*'{ respond testAutoUI, [status: OK] }
        }
    }

    @Transactional
    def delete(TestAutoUI testAutoUI) {

        if (testAutoUI == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        testAutoUI.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'testAutoUI.label', default: 'TestAutoUI'), testAutoUI.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testAutoUI.label', default: 'TestAutoUI'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
