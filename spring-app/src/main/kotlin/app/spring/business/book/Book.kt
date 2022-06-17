package app.spring.business.book

import app.spring.business.author.User
import app.spring.config.BaseEntity
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.http.HttpMethod
import java.time.LocalDateTime
import javax.persistence.*

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
}