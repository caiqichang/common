package spring.gateway.config

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.util.concurrent.Flow.Publisher

//@Component
//class AuthGlobalFilter : GlobalFilter, Ordered {
//    private val log = LoggerFactory.getLogger(AuthGlobalFilter::class.java)
//
//    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
//        if (exchange.request.method == HttpMethod.OPTIONS) return chain.filter(exchange)
//        val tokens = exchange.request.headers["token"]
//        if (tokens?.isNotEmpty() == true) {
//            log.info(tokens.first())
//            exchange.request.headers["fromGateway"] = "true"
//            return chain.filter(exchange)
//        }
//        exchange.response.statusCode = HttpStatus.OK
//        exchange.response.headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE
//        exchange.response.writeAndFlushWith()
//    }
//
//    override fun getOrder(): Int {
//        return -1
//    }
//}