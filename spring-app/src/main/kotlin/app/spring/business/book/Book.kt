package app.spring.business.book

import app.spring.business.author.User
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
@Entity
@NamedEntityGraph(name = "Book.withoutUser", attributeNodes = [NamedAttributeNode("user")])
class Book : Serializable {

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

    @CreatedBy
    var createBy: String? = null

    @LastModifiedBy
    var updateBy: String? = null

    @CreatedDate
    var createTime: LocalDateTime? = null

    @LastModifiedDate
    var updateTime: LocalDateTime? = null

    @Version
    var version: Int? = null

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "bid")
    var user: User? = null
}