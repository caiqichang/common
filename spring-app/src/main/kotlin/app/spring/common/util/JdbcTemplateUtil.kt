package app.spring.common.util

import app.spring.common.db.DBType
import app.spring.common.db.PagingWrapper
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

    private val defaultPagingWrapper = initPagingWrapper()
    private fun initPagingWrapper(): (String, Pageable) -> String {
        val urlSplit = projectProperties.springDatasourceUrl.split(":")
        return PagingWrapper.INSTANCE.getWrapper(if (urlSplit.size > 1) DBType.fromJdbcName(urlSplit[1]) else null)
    }

    fun <T> paging(sql: String, pageable: Pageable, mappedClass: Class<T>, params: Map<String, Any> = emptyMap()
                   , pagingWrapper: (String, Pageable) -> String = defaultPagingWrapper
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
                    BeanPropertyRowMapper.newInstance(mappedClass)
                ), pageable, total
            )
        }
        return PageImpl(emptyList(), pageable, 0L)
    }

}