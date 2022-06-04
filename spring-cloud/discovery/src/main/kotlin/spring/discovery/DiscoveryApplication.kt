package spring.discovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class DiscoveryApplication

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(DiscoveryApplication::class.java)
    builder.application().addListeners(ApplicationPidFileWriter())
    builder.run(* args)
}
