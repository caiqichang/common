package app.spring.controller

import app.spring.annotation.ApiController
import app.spring.annotation.ApiMethod
import org.springframework.web.bind.annotation.PathVariable

@ApiController
class TestController {

    @ApiMethod(["/{i}"])
    fun file(@PathVariable i: Int): String {
        if (i == 0) throw RuntimeException("123")
        return "i is ${i}"
    }
}