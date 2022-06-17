package app.spring.business.book

import app.spring.common.util.JdbcTemplateUtil
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, String>, BookRepositoryExtra {
    fun findAllByNameStartsWith(name: String): List<Book>

    fun findAllByUserId(userId: Int): List<Book>
}

interface BookRepositoryExtra {
    fun customGetAll(): Page<Book>
}

@Component
class BookRepositoryExtraImpl(
    private val jdbcTemplateUtil: JdbcTemplateUtil,
) : BookRepositoryExtra {

    override fun customGetAll(): Page<Book> {
        return jdbcTemplateUtil.paging(
            "SELECT * FROM book ORDER BY id",
            PageRequest.of(0, 10),
            BeanPropertyRowMapper.newInstance(Book::class.java, DefaultConversionService().apply {
                addConverter(object : Converter<String, BookContent> {
                    override fun convert(source: String): BookContent? {
                        return BookContentConverter().convertToEntityAttribute(source)
                    }
                })
            }),
            mapOf(),
        )
    }

}