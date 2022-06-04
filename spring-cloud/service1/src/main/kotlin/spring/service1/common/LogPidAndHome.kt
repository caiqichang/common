package spring.service1.common

import spring.service1.config.data.ProjectProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.system.ApplicationPid
import org.springframework.context.event.EventListener
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
class LogPidAndHome(
    private val projectProperties: ProjectProperties,
) {
    companion object {
        private val log = LoggerFactory.getLogger(LogPidAndHome::class.java)
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun applicationReady(event: ApplicationReadyEvent) {
        log.info("PID: ${ApplicationPid()}")
        log.info("http://localhost:${projectProperties.serverPort}${projectProperties.serverServletContextPath}")
    }
}