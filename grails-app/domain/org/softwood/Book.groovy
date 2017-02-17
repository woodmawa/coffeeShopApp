package org.softwood

class Book {
    String title

    static belongsTo = [author:Author]

    static constraints = {
    }
}
