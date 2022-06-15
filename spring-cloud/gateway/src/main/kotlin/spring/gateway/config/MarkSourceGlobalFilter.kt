package spring.gateway.config

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import spring.gateway.config.data.FilterOrder

@Component
class MarkSourceGlobalFilter : GlobalFilter, Ordered {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        if (exchange.request.method == HttpMethod.OPTIONS) return chain.filter(exchange)

        val mutableExchange = exchange.request.mutate().header("fromGateWay", "true").build()
        return chain.filter(exchange.mutate().request(mutableExchange).build())
    }

    override fun getOrder(): Int {
        return FilterOrder.markSourceGlobalFilter
    }
}