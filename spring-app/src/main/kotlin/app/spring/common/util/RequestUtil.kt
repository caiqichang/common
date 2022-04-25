package app.spring.common.util

import org.springframework.util.StringUtils
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import javax.servlet.http.HttpServletRequest

enum class RequestUtil {
    INSTANCE;

    private val headers = listOf(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
    )

    fun clientIp(): String {
        val request = currentRequest()
        headers.forEach {
            val value = request.getHeader(it)
            if (StringUtils.hasText(value) && value.lowercase() != "unknown") return value
        }
        return request.remoteAddr
    }

    fun currentRequest(): HttpServletRequest {
        return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    }

}