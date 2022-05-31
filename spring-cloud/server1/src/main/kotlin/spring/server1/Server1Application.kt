package spring.server1

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Server1Application

fun main(args: Array<String>) {
	runApplication<Server1Application>(*args)
}
