package app.spring.config

import app.spring.project.ProjectConstants
import app.spring.util.CryptoUtil
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import java.util.regex.Pattern

class EnvironmentPostProcessorConfig : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        val newProperties = mutableMapOf<String, Any>()

        val pattern = Pattern.compile("AES\\[(.+)\\]")
        val decrypt = { k: String, v: Any ->
            val matcher = pattern.matcher(v.toString())
            if (matcher.find()) {
                newProperties[k] = CryptoUtil.INSTANCE.decryptByAes(matcher.group(1), ProjectConstants.propertyAesKey)
            }
        }

        environment?.propertySources?.run {
            forEach {
                if (it is MapPropertySource) {
                    it.source.forEach { (k, v) ->
                        decrypt(k, v)
                    }
                }
            }
            // add to `First` to overwrite the original properties
            addFirst(MapPropertySource("newProperties${Math.random()}", newProperties))
        }
    }
}