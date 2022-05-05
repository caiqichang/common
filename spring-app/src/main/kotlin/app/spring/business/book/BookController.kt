package app.spring.business.book

import app.spring.common.annotation.ApiController
import app.spring.common.annotation.ApiMethod
import org.springframework.web.bind.annotation.RequestBody

@ApiController
class BookController(
    private val bookService: BookService,
) {

    @ApiMethod
    fun findAll(@RequestBody book: Book): List<Book> {
        return bookService.findAllByExample(book)
    }
}