package app.spring.business.book

import app.spring.common.util.JdbcTemplateUtil
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val jdbcTemplate: JdbcTemplate,
    private val jdbcTemplateUtil: JdbcTemplateUtil,
) {

    fun findAllByExample(book: Book): Book {
        return bookRepository.save(book)
    }

    fun customGetAll(): Page<Book> {
        return jdbcTemplateUtil.paging("SELECT * FROM book WHERE author LIKE :author ORDER BY id"
            , PageRequest.of(0, 3)
            , Book::class.java
            , mapOf(
                "author" to "%k%",
            )
        )
    }

    fun test(): List<Book> {
        return bookRepository.findAllByNameStartsWith("he")
    }
}