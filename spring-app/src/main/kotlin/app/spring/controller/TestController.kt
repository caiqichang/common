package app.spring.controller

import app.spring.annotation.ApiController
import app.spring.annotation.ApiMethod
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RequestBody
import java.time.Instant
import java.util.Date

@ApiController
class TestController(
    val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(TestController::class.java)

    @ApiMethod
    fun test1(@RequestBody testDto: TestDto): String {
        return objectMapper.writeValueAsString(testDto)
    }
}

data class TestDto(
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val today: Instant,
)