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
 * 将ApiMethod的响应参数封装为ApiResult
 */
@ControllerAdvice
class ApiMethodResultHandler : ResponseBodyAdvice<Any> {

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.method?.getAnnotation(ApiMethod::class.java) !== null
    }

    override fun beforeBodyWrite(body: Any?, returnType: MethodParameter, selectedContentType: MediaType, selectedConverterType: Class<out HttpMessageConverter<*>>, request: ServerHttpRequest, response: ServerHttpResponse): Any? {
        response.headers.contentType = MediaType.APPLICATION_JSON
        return if (body is String) {
            ObjectMapper().writeValueAsString(ApiResult(data = body))
        }else {
            ApiResult(data = body)
        }
    }
}