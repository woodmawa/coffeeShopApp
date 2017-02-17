package org.softwood

class Author {
    String name

    Collection books

    static hasMany = [books:Book]

    static constraints = {
        books nullable:true, unique:true
    }
}
