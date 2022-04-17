package app.spring.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.system.ApplicationPid
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
class LogPidAndHome(
    @Value("\${server.port:8080}") val serverPort: Int,
    @Value("\${server.servlet.context-path:}") val serverServletContextPath: String,
) {
    companion object {
        private val log = LoggerFactory.getLogger(LogPidAndHome::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun applicationReady() {
        log.info("PID: ${ApplicationPid()}")
        log.info("http://localhost:$serverPort$serverServletContextPath")
    }
}