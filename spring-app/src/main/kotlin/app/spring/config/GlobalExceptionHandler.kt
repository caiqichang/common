package app.spring.config

import app.spring.common.api.ApiResult
import app.spring.common.api.ApiState
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(Throwable::class)
    fun throwable(t: Throwable): ApiResult<Any> {
        t.printStackTrace()
        log.info(t.message)
        return ApiResult(
            state = ApiState.ERROR,
            message = t.message,
        )
    }
}