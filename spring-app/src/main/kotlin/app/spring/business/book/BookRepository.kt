package app.spring.business.book

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String> {


    fun findAllByNameStartsWith(name: String): List<Book>
}