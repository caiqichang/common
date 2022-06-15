package spring.service1.business

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import spring.service1.business.remote.service2.TestRemote
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class TestController(
    private val testRemote: TestRemote,
) {

    companion object {
        private val log = LoggerFactory.getLogger(TestController::class.java)
    }

    @RequestMapping("/t0")
    fun t0(request: HttpServletRequest): String {
        log.info(request.getHeader("fromGateway"))
        log.info(request.getHeader("token"))
        return testRemote.t0()
    }

    @RequestMapping("/login")
    fun login(): String {
        return "login"
    }
}