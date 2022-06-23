package app.spring.business.author

import app.spring.business.book.Book
import app.spring.config.data.AutoIncrementId
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.TableGenerator
import javax.persistence.Transient

@Entity
class User {

    companion object {
        private const val name_ = "User.id"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = name_)
    @TableGenerator(
        name = name_, pkColumnValue = name_,
        table = AutoIncrementId.tableName, allocationSize = 1,
        pkColumnName = AutoIncrementId.pkColumnName, valueColumnName = AutoIncrementId.valueColumnName
    )
    var id: Int? = null

    var name: String? = null

    var bid: Int? = null

    @Transient
    var books: List<Book>? = null

}