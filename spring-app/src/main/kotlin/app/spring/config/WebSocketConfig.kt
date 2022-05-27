package app.spring.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@EnableWebSocket
@Configuration
class WebSocketConfig(
    private val authenticationWebSocketInterceptor: AuthenticationWebSocketInterceptor,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        val handlers = authenticationWebSocketInterceptor.webSocketHandlers
        if (handlers.isNotEmpty()) {
            var registration = registry.addHandler(handlers.first(), * handlers.first().getPaths().toTypedArray())
            if (handlers.size > 1) {
                for (i in 1 until handlers.size) {
                    registration = registration.addHandler(handlers[i], * handlers[i].getPaths().toTypedArray())
                }
            }
            registration.addInterceptors(authenticationWebSocketInterceptor)
        }
    }
}