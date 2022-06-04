package spring.service1.business

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.service1.business.remote.service2.TestRemote

@RestController
@RequestMapping("/test")
class TestController(
    private val testRemote: TestRemote,
) {

    @RequestMapping("/t0")
    fun t0(): String {
        return testRemote.t0()
    }
}