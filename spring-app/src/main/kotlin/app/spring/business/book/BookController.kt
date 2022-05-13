package app.spring.business.book

import app.spring.common.annotation.ApiController
import app.spring.common.annotation.ApiMethod
import app.spring.common.db.router.DatasourceRouterProp
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.RequestBody

@ApiController
class BookController(
    private val bookService: BookService,
    private val datasourceRouterProp: DatasourceRouterProp,
) {

    @ApiMethod
    fun findAll(@RequestBody book: Book): Book {
        return bookService.findAllByExample(book)
    }

    @ApiMethod
    fun page(): Page<Book> {
        return bookService.customGetAll()
    }

    @ApiMethod
    fun test(): List<Book> {
        return bookService.test()
    }

    @ApiMethod
    fun prop(): DatasourceRouterProp {
        return datasourceRouterProp
    }
}