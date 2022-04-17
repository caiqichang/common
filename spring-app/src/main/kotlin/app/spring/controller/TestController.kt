package app.spring.controller

import app.spring.annotation.ApiController
import app.spring.annotation.ApiMethod

@ApiController
class TestController {

    @ApiMethod
    fun test1(): String {
        return "call test1"
    }
}