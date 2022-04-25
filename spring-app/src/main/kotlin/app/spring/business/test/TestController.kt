package app.spring.business.test

import app.spring.common.annotation.ApiController
import app.spring.common.annotation.ApiMethod
import app.spring.common.util.DataObjectUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import java.time.Instant

@ApiController
class TestController(
    val objectMapper: ObjectMapper,
    private val dataObjectUtil: DataObjectUtil,
) {

    private val log = LoggerFactory.getLogger(TestController::class.java)

    @ApiMethod
    fun test1(@RequestBody testDto: TestDto): String {
        return objectMapper.writeValueAsString(testDto)
    }

    @ApiMethod
    fun test2(): String {
        log.info("${dataObjectUtil.copy(TestDto(Instant.now())).today}")
        return "ok"
    }
}

data class TestDto(
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val today: Instant,
)