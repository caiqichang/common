package app.spring.common.util

import app.spring.config.data.ProjectProperties
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class JdbcTemplateUtil(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    private val projectProperties: ProjectProperties,
) {

    fun <T> paging(sql: String, pageable: Pageable, mappedClass: Class<T>, params: Map<String, Any> = emptyMap()): Page<T> {
        val total = namedParameterJdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM ( ${sql} ) AS BUSINESS_TABLE",
            params, Long::class.java
        )
        if (total != null && total > 0) {
            return PageImpl(
                namedParameterJdbcTemplate.query(
                    wrappingAsPaging(sql, pageable),
                    params,
                    BeanPropertyRowMapper.newInstance(mappedClass)
                ), pageable, total
            )
        }
        return PageImpl(emptyList(), pageable, 0L)
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

    private fun wrappingAsPaging(sql: String, pageable: Pageable): String {
        if (projectProperties.springDatasourceUrl.startsWith("jdbc:mysql", true)) {
            return wrapperAsMySQLPaging(sql, pageable)
        }
        if (projectProperties.springDatasourceUrl.startsWith("jdbc:sqlserver", true)) {
            return wrapperAsSQLServerPaging(sql, pageable)
        }
        return sql
    }

    private fun wrapperAsMySQLPaging(sql: String, pageable: Pageable): String {
        return """
            SELECT * FROM (
                SELECT (@row := @row + 1) AS ROW_COLUMN, BUSINESS_TABLE.* FROM (SELECT @row := 0) AS ROW_TABLE, (
                    ${sql}  ${getSort(pageable)} 
                ) AS BUSINESS_TABLE
            ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_COLUMN > (${pageable.pageNumber} * ${pageable.pageSize}) LIMIT ${pageable.pageSize}
        """.trimIndent()
    }

    private fun wrapperAsSQLServerPaging(sql: String, pageable: Pageable): String {
        var sort = getSort(pageable)
        if (sort.isEmpty()) sort = "ORDER BY ${sql.split(Regex("ORDER BY", RegexOption.IGNORE_CASE)).last()}"
        return """
            SELECT TOP ${pageable.pageSize} * FROM ( 
                SELECT ROW_NUMBER() OVER ( ${sort} ) AS ROW_COLUMN, BUSINESS_TABLE.* FROM (
                    ${sql}
                ) AS BUSINESS_TABLE
            ) AS BUSINESS_TABLE_WITH_ROW WHERE ROW_COLUMN > (${pageable.pageNumber} * ${pageable.pageSize})
        """.trimIndent()
    }
}