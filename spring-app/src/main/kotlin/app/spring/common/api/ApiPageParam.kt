package app.spring.common.api

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order
import org.springframework.util.StringUtils

/**
 * Base pagination parameters
 */
open class ApiPage {
    val pagination = ApiPagination()

    fun toPageable(propertyMap: Map<String, String>): Pageable {
        return PageRequest.of(
            pagination.pageNumber,
            pagination.pageSize,
            Sort.by(
                pagination.sort
                    .filter { StringUtils.hasText(propertyMap[it.property]) }
                    .map { Order(it.direction, propertyMap[it.property]!!) }
                    .toList()
            )
        )
    }
}

class ApiPagination {
    val pageSize = 10
    val pageNumber = 0
    val sort = listOf<ApiPaginationOrder>()
}

class ApiPaginationOrder {
    val property: String = ""
    val direction = Direction.ASC
}