package app.spring.config.data

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ProjectProperties(

    @Value("\${spring.jackson.dateformat:yyyy-MM-dd HH:mm:ss}")
    val springJacksonDateformat: String,

    @Value("\${server.port:8080}")
    val serverPort: Int,

    @Value("\${server.servlet.context-path:}")
    val serverServletContextPath: String,

    @Value("\${spring.datasource.url:}")
    val springDatasourceUrl: String,

    )