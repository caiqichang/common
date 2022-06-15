package spring.gateway.config

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import java.nio.charset.StandardCharsets

@Component
class AuthenticationGatewayFilterFactory : AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config>(Config::class.java) {
    companion object {
        private val log = LoggerFactory.getLogger(AuthenticationGatewayFilterFactory::class.java)
    }

    override fun apply(config: Config?): GatewayFilter? {
        // grab configuration from Config object
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->

            if (exchange.request.method == HttpMethod.OPTIONS) return@GatewayFilter chain.filter(exchange)

            val token = exchange.request.headers["token"]
            if (token != null) {
                log.info(token.toString())
                val mutableExchange = exchange.request.mutate().header("token", "admin").build()
                chain.filter(exchange.mutate().request(mutableExchange).build())
            }else {
                exchange.response.statusCode = HttpStatus.OK
                exchange.response.headers[HttpHeaders.CONTENT_TYPE] = "${MediaType.APPLICATION_JSON_VALUE};charset=${StandardCharsets.UTF_8}"
                exchange.response.writeWith(Flux.just(
                    exchange.response.bufferFactory().wrap("""
                        {
                          "state": "ERROR",
                          "message": "Authentication Failed"
                        }
                    """.trimIndent().toByteArray())
                ))
            }
        }
    }

    class Config
}