package app.spring.business.book

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Book {

    @Id
    var id: String? = null

    var author: String? = null

    var name: String? = null
}