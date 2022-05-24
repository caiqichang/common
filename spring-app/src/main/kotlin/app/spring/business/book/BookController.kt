package app.spring.business.book

import app.spring.common.api.ApiController
import app.spring.common.api.ApiMethod
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.RequestBody

@ApiController
class BookController(
    private val bookService: BookService,
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

}