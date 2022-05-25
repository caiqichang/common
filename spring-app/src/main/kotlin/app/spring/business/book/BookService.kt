package app.spring.business.book

import app.spring.common.db.router.DB
import app.spring.common.util.JdbcTemplateUtil
import app.spring.config.data.DataSourceKey
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val jdbcTemplateUtil: JdbcTemplateUtil,
) {

    fun findAllByExample(book: Book): Book {
        return bookRepository.save(book)
    }

    @DB(DataSourceKey.DB2)
    fun customGetAll(): Page<Book> {
        return jdbcTemplateUtil.paging("SELECT * FROM book ORDER BY id"
            , PageRequest.of(0, 10)
            , Book::class.java
            , mapOf()
        )
    }

//    @DB(DataSourceKey.DB1)
    fun test(): List<Book> {
        return bookRepository.findAll()
    }
}