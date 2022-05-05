package app.spring.business.book

import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class BookService(
    private val bookRepository: BookRepository,
) {

    fun findAllByExample(book: Book): List<Book> {
        val matcher = ExampleMatcher.matching()

        if (StringUtils.hasText(book.author)) {
            matcher.withMatcher("author", ExampleMatcher.GenericPropertyMatchers.contains())
        }

        if (StringUtils.hasText(book.name)) {
            matcher.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.regex())
        }

        return bookRepository.findAll(Example.of(book, matcher))
    }
}