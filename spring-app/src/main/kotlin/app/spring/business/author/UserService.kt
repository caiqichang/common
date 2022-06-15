package app.spring.business.author

import app.spring.business.book.BookRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
        private val userRepository: UserRepository,
        private val bookRepository: BookRepository,
) {

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }

    @Transactional
    fun save(user: User): User {
        return userRepository.save(user)
    }

//    @Transactional(readOnly = true)
    fun findAll(): List<User> {
        val list = userRepository.findAll()
        list.parallelStream().forEach {
           it.bid?.run {
               it.books = bookRepository.findAllByUserId(this)
           }
        }
        return list
    }
}