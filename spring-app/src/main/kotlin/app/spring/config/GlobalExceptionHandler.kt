package app.spring.config

import app.spring.util.ApiResult
import app.spring.util.ApiState
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Throwable::class)
    fun throwable(t: Throwable): ResponseEntity<ApiResult<Any>> {
        t.printStackTrace()
        log.info(t.message)
        return ResponseEntity.ok(ApiResult(
            state = ApiState.ERROR,
            message = t.message,
        ))
    }
}