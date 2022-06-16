package app.spring.business.author

import app.spring.common.api.ApiController
import app.spring.common.api.ApiMethod
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestBody

@ApiController
class UserController(
        private val userService: UserService,
) {
    companion object {
        private val log = LoggerFactory.getLogger(UserController::class.java)
    }

    @ApiMethod
    fun save(@RequestBody user: User): User {
        return userService.save(user)
    }

    @ApiMethod
    fun list(): List<User> {
        return userService.findAll()
    }
}