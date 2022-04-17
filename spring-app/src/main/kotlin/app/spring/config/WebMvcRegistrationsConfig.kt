package app.spring.config

import app.spring.annotation.processor.ApiRequestMappingHandlerMapping
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping

@Configuration
class WebMvcRegistrationsConfig : WebMvcRegistrations {

    /**
     * 自定义RequestMapping处理
     */
    override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return ApiRequestMappingHandlerMapping()
    }
}