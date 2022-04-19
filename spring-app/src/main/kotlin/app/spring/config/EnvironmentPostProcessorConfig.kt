package app.spring.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

class EnvironmentPostProcessorConfig : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        val newProperties = mutableMapOf<String, Any>()
        environment?.propertySources?.run {
            forEach {
                if (it is MapPropertySource) {
                    it.source.forEach { (k, v) ->
                        // todo

                        // newProperties[k] = v
                    }
                }
            }
            addFirst(MapPropertySource("newProperties${Math.random()}", newProperties))
        }
    }
}