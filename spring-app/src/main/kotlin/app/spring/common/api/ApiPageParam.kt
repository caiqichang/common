package app.spring.common.api

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order

/**
 * Base pagination parameters
 */
open class ApiPage {
    val pagination = ApiPagination()
    fun toPageable(): Pageable {
        return PageRequest.of(
            pagination.pageNumber,
            pagination.pageSize,
            Sort.by(
                pagination.sort
                    .filter { it.property.isNotBlank() }
                    .map { Order(it.direction, it.property) }
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