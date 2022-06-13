package app.spring.business.author

import app.spring.common.api.ApiController
import app.spring.common.api.ApiMethod
import org.springframework.web.bind.annotation.RequestBody

@ApiController
class UserController(
        private val userService: UserService,
) {

    @ApiMethod
    fun save(@RequestBody user: User): User {
        return userService.save(user)
    }

    @ApiMethod
    fun list(): List<User> {
        return userService.findAll()
    }
}