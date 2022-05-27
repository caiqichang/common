package app.spring.common.db.wrapper

import app.spring.common.db.DBType
import org.slf4j.LoggerFactory

enum class KeywordWrapper {
    INSTANCE;

    companion object {
        private val log = LoggerFactory.getLogger(PagingWrapper::class.java)
    }

    fun getWrapper(dbType: DBType?): Pair<String, String> {
        return when(dbType) {
            DBType.MySQL -> "`" to "`"
            DBType.SQLServer -> "[" to "]"
            DBType.Postgresql -> "\"" to "\""
            null -> {
                log.warn("keyword wrapper not found")
                "" to ""
            }
        }
    }
}