package app.spring.business.book

import app.spring.common.util.JdbcTemplateUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>, BookRepositoryExtra {
    fun findAllByNameStartsWith(name: String): List<Book>

    @EntityGraph("Book.withoutUser", type = EntityGraph.EntityGraphType.LOAD)
    override fun findAll(): List<Book>
}

interface BookRepositoryExtra {
    fun customGetAll(): Page<Book>
}

@Component
class BookRepositoryExtraImpl(
        private val jdbcTemplateUtil: JdbcTemplateUtil,
): BookRepositoryExtra {

    override fun customGetAll(): Page<Book> {
        return jdbcTemplateUtil.paging("SELECT * FROM book ORDER BY id"
                , PageRequest.of(0, 10)
                , Book::class.java
                , mapOf()
        )
    }

}