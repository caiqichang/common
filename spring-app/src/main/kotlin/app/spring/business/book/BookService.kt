package app.spring.business.book

import app.spring.common.util.JdbcTemplateUtil
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val jdbcTemplateUtil: JdbcTemplateUtil,
) {

    fun findAllByExample(book: Book): Book {
        return bookRepository.save(book)
    }

    //    @DB(DataSourceKey.DB2)
    fun customGetAll(): Page<Book> {
        return bookRepository.customGetAll()
    }

    //    @DB(DataSourceKey.DB1)
//    @Transactional(readOnly = true)
    fun test(): List<Book> {
        val list = bookRepository.findAll()
        return list
    }

    @Transactional
    fun save(book: Book): Book {
        return bookRepository.save(book)
    }
}