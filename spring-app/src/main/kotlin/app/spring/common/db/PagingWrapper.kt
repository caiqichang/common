package app.spring.common.db

import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable

enum class PagingWrapper {
    INSTANCE;

    companion object {
        private val log = LoggerFactory.getLogger(PagingWrapper::class.java)
    }

    fun getWrapper(dbType: DBType?): (String, Pageable) -> String {
        return mapOf(

            DBType.MySQL to value@{ sql: String, pageable: Pageable ->
                // before mysql 8
//                return@value """
//                    SELECT * FROM (
//                        SELECT (@row := @row + 1) AS ROW_INDEX, BUSINESS_TABLE.* FROM (SELECT @row := 0) AS ROW_TABLE, (
//                            $sql  ${getSort(pageable)}
//                        ) AS BUSINESS_TABLE
//                    ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_INDEX > (${pageable.pageNumber} * ${pageable.pageSize}) LIMIT ${pageable.pageSize}
//                """.trimIndent()

                var sort = getSort(pageable)
                if (sort.isEmpty()) {
                    sort = getSort(sql)
                    if (sort.isEmpty()) {
                        return@value "$sql LIMIT ${pageable.pageSize} OFFSET ${pageable.pageNumber * pageable.pageSize}"
                    }
                }
                return@value """
                    SELECT * FROM ( 
                        SELECT ROW_NUMBER() OVER ( $sort ) AS ROW_INDEX, BUSINESS_TABLE.* FROM (
                            $sql
                        ) AS BUSINESS_TABLE
                    ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_INDEX > ${pageable.pageNumber * pageable.pageSize} LIMIT ${pageable.pageSize}
                """.trimIndent()
            },

            DBType.SQLServer to value@{ sql: String, pageable: Pageable ->
                var sort = getSort(pageable)
                if (sort.isEmpty()) {
                    sort = getSort(sql)
                    if (sort.isEmpty()) throw RuntimeException("Paging sql must contain sorting")
                }
                return@value """
                    SELECT TOP ${pageable.pageSize} * FROM ( 
                        SELECT ROW_NUMBER() OVER ( $sort ) AS ROW_INDEX, BUSINESS_TABLE.* FROM (
                            $sql
                        ) AS BUSINESS_TABLE
                    ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_INDEX > ${pageable.pageNumber * pageable.pageSize}
                """.trimIndent()
            },

            DBType.Postgresql to value@{ sql: String, pageable: Pageable ->
                var sort = getSort(pageable)
                if (sort.isEmpty()) {
                    sort = getSort(sql)
                    if (sort.isEmpty()) {
                        return@value "$sql LIMIT ${pageable.pageSize} OFFSET ${pageable.pageNumber * pageable.pageSize}"
                    }
                }
                return@value """
                    SELECT * FROM ( 
                        SELECT ROW_NUMBER() OVER ( $sort ) AS ROW_INDEX, BUSINESS_TABLE.* FROM (
                            $sql
                        ) AS BUSINESS_TABLE
                    ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_INDEX > ${pageable.pageNumber * pageable.pageSize} LIMIT ${pageable.pageSize}
                """.trimIndent()
            },

        )[dbType] ?: value@{ sql, _ ->
            log.warn("Paging wrapper not found")
            return@value sql
        }
    }

    private fun getSort(pageable: Pageable): String {
        return if (pageable.sort.isEmpty) {
            ""
        } else {
            "ORDER BY ${
                pageable.sort.stream().map {
                    "${it.property} ${it.direction}"
                }.toList().joinToString(",")
            }"
        }
    }

    private fun getSort(sql: String): String {
        val sortSplit = sql.split(Regex("ORDER BY", RegexOption.IGNORE_CASE))
        if (sortSplit.size < 2) return ""
        return "ORDER BY ${sortSplit.last()}"
    }
}