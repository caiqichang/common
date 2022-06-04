package spring.service2

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @RequestMapping("/t0")
    fun t0(): String {
        return "call service 2 t0"
    }
}