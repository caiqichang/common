package app.spring.config

import app.spring.common.util.CryptoUtil
import app.spring.config.data.ProjectConstants
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.env.OriginTrackedMapPropertySource
import org.springframework.core.env.ConfigurableEnvironment
import java.util.regex.Pattern

class EnvironmentPostProcessorConfig : EnvironmentPostProcessor {

    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        val pattern = Pattern.compile("AES\\[(.+)]")
        environment?.propertySources?.run {
            forEach {
                if (it is OriginTrackedMapPropertySource) {
                    val newProperties = mutableMapOf<String, Any>()
                    it.source.forEach { (k, v) ->
                        val matcher = pattern.matcher(v.toString())
                        newProperties[k] = if (matcher.find())
                             CryptoUtil.INSTANCE.decryptByAes(matcher.group(1), ProjectConstants.propertyAesKey)
                        else v
                    }

                    replace(it.name, OriginTrackedMapPropertySource(it.name, newProperties, true))
                }
            }
        }
    }
}
