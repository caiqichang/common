package spring.service1.business.remote.service2

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping

@FeignClient(name = "service2", contextId = "service2Test", path = "/service2/test")
interface TestRemote {

    @RequestMapping("/t0")
    fun t0(): String
}