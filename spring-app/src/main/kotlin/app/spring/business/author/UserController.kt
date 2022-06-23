package app.spring.business.author

import app.spring.common.api.ApiController
import app.spring.common.api.ApiMethod
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

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

    @ApiMethod
    fun delay(t: Int): String {
        Thread.sleep(t * 1000L)
        return "delay ${t}s"
    }

    @RequestMapping("/webclient")
    @ResponseBody
    fun webclient(): String {
        return """
            {
              "id": "123",
              "name": "mark"
            }
        """.trimIndent()
    }
}