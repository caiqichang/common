package app.spring.common

import app.spring.config.data.ApplicationProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.system.ApplicationPid
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
class LogPidAndHome(
    private val applicationProperties: ApplicationProperties,
) {
    companion object {
        private val log = LoggerFactory.getLogger(LogPidAndHome::class.java)
    }

    @EventListener(ApplicationReadyEvent::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun applicationReady() {
        log.info("PID: ${ApplicationPid()}")
        log.info("http://localhost:${applicationProperties.serverPort}${applicationProperties.serverServletContextPath}")
    }
}