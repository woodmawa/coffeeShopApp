package org.softwood

//means to get gorm to map as ordinal rather than the default String form
enum StarRating {
    None (0),
    OneStar (1),
    TwoStar (2),
    ThreeStar (3),
    FourStar (4),
    FiveStar (5)

    final int id
    private StarRating (int id) {
        this.id = id
    }
}

class Rating implements Serializable {

    StarRating stars
    Post post

    static belongsTo = [post:Post]

    static constraints = {
    }
}

