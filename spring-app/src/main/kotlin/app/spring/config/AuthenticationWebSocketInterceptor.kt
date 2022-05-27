package app.spring.config

import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Component
class AuthenticationWebSocketInterceptor : HandshakeInterceptor {

    val webSocketHandlers = listOf<WebSocketController>(
        // add websocketHandler here
    )

    override fun beforeHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, attributes: MutableMap<String, Any>): Boolean {
        val subProtocols = request.headers[WebSocketHttpHeaders.SEC_WEBSOCKET_PROTOCOL]

        // authenticate here

        // response.headers.add(WebSocketHttpHeaders.SEC_WEBSOCKET_PROTOCOL, subProtocol)
        return true
    }

    override fun afterHandshake(request: ServerHttpRequest, response: ServerHttpResponse, wsHandler: WebSocketHandler, exception: Exception?) {

    }
}

/**
 * websocket with paths
 */
interface WebSocketController : WebSocketHandler {
    /**
     * handle paths
     */
    fun getPaths(): List<String>
}