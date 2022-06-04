package spring.service2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class Service2Application

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(Service2Application::class.java)
    builder.application().addListeners(ApplicationPidFileWriter())
    builder.run(* args)
}
