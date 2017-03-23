package org.softwood.auto

import java.time.LocalDateTime

class TestAutoUI {

    String name
    LocalDateTime dateCreated
    boolean isHappy

    static constraints = {
        name()
        dateCreated()
        isHappy nullable:true
    }
}
