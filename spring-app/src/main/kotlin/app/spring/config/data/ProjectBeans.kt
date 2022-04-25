package app.spring.config.data

import app.spring.common.util.DataObjectUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ProjectBeans {

    @Bean
    fun dataObjectUtil(om: ObjectMapper): DataObjectUtil {
        return DataObjectUtil(om)
    }
}