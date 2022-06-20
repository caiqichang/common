package app.spring.business.book

import app.spring.business.author.User
import app.spring.config.BaseEntity
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.support.DefaultConversionService
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.http.HttpMethod
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import java.time.LocalDateTime
import javax.persistence.*
import kotlin.jvm.Transient

@EntityListeners(AuditingEntityListener::class)
@Entity
class Book : BaseEntity() {

    @PrePersist
    fun defaultValue() {
        if (release == null) release = LocalDateTime.now()
    }

    @Id
    var id: String? = null

    var author: String? = null

    var name: String? = null

    @Column(name = "[release]")
    var release: LocalDateTime? = null

    var content: BookContent? = null

    var userId: Int? = null

    @Transient
    var user: User? = null

    @Enumerated(EnumType.STRING)
    var type: HttpMethod? = null

    companion object {
        fun rowMapper(): RowMapper<Book> {
            return BeanPropertyRowMapper.newInstance(Book::class.java, DefaultConversionService().apply {
                val anonymous = object : Converter<String, BookContent> {
                    override fun convert(source: String): BookContent? {
                        return BookContentConverter().convertToEntityAttribute(source)
                    }
                }

                val lambda = Converter<String, BookContent> { BookContentConverter().convertToEntityAttribute(it) }

                println("anonymous: ${anonymous.javaClass.genericInterfaces.joinToString { it.typeName }}")
                println("lambda: ${lambda.javaClass.genericInterfaces.joinToString { it.typeName }}")

                addConverter(anonymous)
            })
        }
    }
}