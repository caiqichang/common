package spring.service1.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.slf4j.LoggerFactory
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import spring.service1.business.remote.FeignIndex

@Configuration
@EnableFeignClients(basePackageClasses = [FeignIndex::class])
class FeignConfig {
}

@Component
class FeignInterceptor : RequestInterceptor {

    companion object {
        private val log = LoggerFactory.getLogger(FeignInterceptor::class.java)
    }

    override fun apply(template: RequestTemplate?) {
        log.info("will call ${template?.url()}")
    }

}