package app.spring.business.author

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
        private val userRepository: UserRepository,
) {

    @Transactional
    fun save(user: User): User {
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<User> {
        val list = userRepository.findAll()
//        list.forEach { it.books }
        return list
    }
}