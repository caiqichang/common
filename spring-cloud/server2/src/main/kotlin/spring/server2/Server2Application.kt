package spring.server2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Server2Application

fun main(args: Array<String>) {
	runApplication<Server2Application>(*args)
}
