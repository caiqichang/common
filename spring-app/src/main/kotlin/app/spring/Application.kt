package app.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.ApplicationPidFileWriter

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    val builder = SpringApplicationBuilder(Application::class.java)
    builder.application().addListeners(ApplicationPidFileWriter())
    builder.run(* args)
}
