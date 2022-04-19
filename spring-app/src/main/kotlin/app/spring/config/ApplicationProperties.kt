package app.spring.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApplicationProperties(

    @Value("\${spring.jackson.dateformat:yyyy-MM-dd HH:mm:ss}")
    val springJacksonDateformat: String,

    @Value("\${spring.jackson.timezone:GMT+8}")
    val springJacksonTimezone: String,

    @Value("\${server.port:8080}")
    val serverPort: Int,

    @Value("\${server.servlet.context-path:}")
    val serverServletContextPath: String,

)