package spring.service1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class Service1Application

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(Service1Application::class.java)
    builder.application().addListeners(ApplicationPidFileWriter())
    builder.run(* args)
}
