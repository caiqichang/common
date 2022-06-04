package spring.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(GatewayApplication::class.java)
    builder.application().addListeners(ApplicationPidFileWriter())
    builder.run(* args)
}
