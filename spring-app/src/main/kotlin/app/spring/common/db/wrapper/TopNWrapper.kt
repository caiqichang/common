package app.spring.common.db.wrapper

import app.spring.common.db.DBType
import org.slf4j.LoggerFactory

enum class TopNWrapper {
    INSTANCE;

    companion object {
        private val log = LoggerFactory.getLogger(PagingWrapper::class.java)
    }

    fun getWrapper(dbType: DBType?): (String, Int) -> String {
        return mapOf(
            DBType.MySQL to value@{ sql: String, n: Int ->
                return@value "$sql LIMIT $n"
            },

            DBType.SQLServer to value@{ sql: String, n: Int ->
                return@value sql.replaceFirst("SELECT", "SELECT TOP $n ", ignoreCase = true)
            },

            DBType.Postgresql to value@{ sql: String, n: Int ->
                return@value "$sql LIMIT $n"
            },

        )[dbType] ?: value@{ sql, _ ->
            log.warn("TopN wrapper not found")
            return@value sql
        }
    }
}