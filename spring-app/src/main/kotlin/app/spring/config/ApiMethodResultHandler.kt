package app.spring.config

import app.spring.annotation.ApiMethod
import app.spring.util.ApiResult
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * wrapper the result of [ApiMethod] as [ApiResult]
 */
@ControllerAdvice
class ApiMethodResultHandler(
    private val objectMapper: ObjectMapper,
) : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.method?.getAnnotation(ApiMethod::class.java) !== null
    }

    override fun beforeBodyWrite(body: Any?, returnType: MethodParameter, selectedContentType: MediaType, selectedConverterType: Class<out HttpMessageConverter<*>>, request: ServerHttpRequest, response: ServerHttpResponse): Any? {
        // if body is string, content-type of response will be text/plain
        response.headers.contentType = MediaType.APPLICATION_JSON
        return if (body is String) {
            // text/plain will not be converted to json automatically
            objectMapper.writeValueAsString(ApiResult(data = body))
        }else {
            ApiResult(data = body)
        }
    }
}