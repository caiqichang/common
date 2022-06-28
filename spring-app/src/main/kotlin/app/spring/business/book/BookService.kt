package app.spring.business.book

import app.spring.common.util.JdbcTemplateUtil
import app.spring.config.data.CacheNames
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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

    @Cacheable(cacheNames = [CacheNames.bookCache])
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

    @CacheEvict(cacheNames = [CacheNames.bookCache], allEntries = true)
    @Transactional
    fun save(book: Book): Book {
        return bookRepository.save(book)
    }

}


