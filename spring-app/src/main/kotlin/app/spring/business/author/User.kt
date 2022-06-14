package app.spring.business.author

import app.spring.config.data.AutoIncrementId
import java.io.Serializable
import javax.persistence.*

@Entity
class User : Serializable {

    companion object {
        private const val name_ = "User.id"
    }

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = name_)
    @TableGenerator(name = name_, pkColumnValue = name_,
            table = AutoIncrementId.tableName, allocationSize = 1,
            pkColumnName = AutoIncrementId.pkColumnName, valueColumnName = AutoIncrementId.valueColumnName)
    val id: Int? = null

    val name: String? = null

    val bid: Int? = null

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
//    val books: List<Book>? = null
}