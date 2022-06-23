package app.spring.common.util

import app.spring.common.db.DBType
import app.spring.common.db.wrapper.KeywordWrapper
import app.spring.common.db.wrapper.PagingWrapper
import app.spring.common.db.wrapper.TopNWrapper
import app.spring.config.data.ProjectProperties
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component

@Component
class JdbcTemplateUtil(
    private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
    projectProperties: ProjectProperties,
) {

    private val defaultDBType = projectProperties.springDatasourceUrl.split(":").let {
        if (it.size > 1) DBType.fromJdbcName(it[1]) else null
    }

    private val defaultPagingWrapper = PagingWrapper.INSTANCE.getWrapper(defaultDBType)
    private val defaultTopNWrapper = TopNWrapper.INSTANCE.getWrapper(defaultDBType)
    val defaultKeywordWrapper = KeywordWrapper.INSTANCE.getWrapper(defaultDBType)

    fun <T> paging(
        sql: String, pageable: Pageable, rowMapper: RowMapper<T>, params: Map<String, Any> = emptyMap(), pagingWrapper: (String, Pageable) -> String = defaultPagingWrapper
    ): Page<T> {
        val total = namedParameterJdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM ( $sql ) AS BUSINESS_TABLE",
            params, Long::class.java
        )
        if (total != null && total > 0) {
            return PageImpl(
                namedParameterJdbcTemplate.query(
                    pagingWrapper(sql, pageable),
                    params,
                    rowMapper,
                ), pageable, total
            )
        }
        return PageImpl(mutableListOf(), pageable, 0L)
    }

    fun <T> topN(
        sql: String, n: Int, rowMapper: RowMapper<T>, params: Map<String, Any> = emptyMap(), topNWrapper: (String, Int) -> String = defaultTopNWrapper
    ): List<T> {
        return namedParameterJdbcTemplate.query(
            topNWrapper(sql, n),
            params,
            rowMapper,
        )
    }
}