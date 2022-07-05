package app.spring.common.db.wrapper

import app.spring.common.db.DBType
import org.slf4j.LoggerFactory

object KeywordWrapper {

    private val log = LoggerFactory.getLogger(KeywordWrapper::class.java)

    fun getWrapper(dbType: DBType?): Pair<String, String> {
        return when (dbType) {
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