package app.spring.controller

import app.spring.annotation.ApiController
import app.spring.annotation.ApiMethod
import org.slf4j.LoggerFactory

@ApiController
class TestController {

    private val log = LoggerFactory.getLogger(TestController::class.java)

    @ApiMethod
    fun test1(i: Int): String {
        log.info("---- in controller")
        if (i == 0) throw RuntimeException("---- 123")
        return "call test1"
    }
}