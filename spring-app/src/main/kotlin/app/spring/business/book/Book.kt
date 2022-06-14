package app.spring.business.book

import app.spring.business.author.User
import app.spring.config.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@Entity
@NamedEntityGraph(name = "Book.withoutUser", attributeNodes = [NamedAttributeNode("user")])
class Book : BaseEntity(), Serializable {

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

    @Convert(converter = BookContentConverter::class)
    var content: BookContent? = null

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "bid")
    var user: User? = null
}