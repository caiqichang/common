package spring.service2

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/test")
class TestController {

    companion object {
        private val log = LoggerFactory.getLogger(TestController::class.java)
    }

    @RequestMapping("/t0")
    fun t0(request: HttpServletRequest): String {
        log.info(request.getHeader("fromClient"))
        return "call service 2 t0"
    }
}