package app.spring.config

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationInterceptor(
    authenticationWebSocketInterceptor: AuthenticationWebSocketInterceptor,
) : HandlerInterceptor {

    val includePaths = listOf(
        "/**",
    )

    val excludePaths = mutableListOf(
        "/",
        "/login",
        "/book/page",
    )

    init {
        // exclude websocket paths
        authenticationWebSocketInterceptor.webSocketHandlers.forEach {
            excludePaths.addAll(it.getPaths())
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        // allow options request for CORS
        if (request.method == HttpMethod.OPTIONS.name) return true

        // authenticate here

        return true
    }
}