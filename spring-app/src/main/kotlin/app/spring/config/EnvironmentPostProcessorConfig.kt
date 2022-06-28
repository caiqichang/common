package app.spring.config

import app.spring.common.util.CryptoUtil
import app.spring.config.data.ProjectConstants
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.env.OriginTrackedMapPropertySource
import org.springframework.core.env.ConfigurableEnvironment
import java.util.regex.Pattern

class EnvironmentPostProcessorConfig : EnvironmentPostProcessor {

    companion object {
        private val pattern = Pattern.compile("AES\\[(.+)]")
    }

    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        environment?.propertySources?.run {
            forEach {
                if (it is OriginTrackedMapPropertySource) {
                    val newSource = mutableMapOf<String, Any>()
                    it.source.forEach { (k, v) -> newSource[k] = overwriteProperty(k, v) }
                    replace(it.name, OriginTrackedMapPropertySource(it.name, newSource, true))
                }
            }

            addFirst(OriginTrackedMapPropertySource("${Math.random()}", addNewProperties(), true))
        }
    }

    private fun overwriteProperty(key: String, value: Any): Any {
        val matcher = pattern.matcher(value.toString())
        if (matcher.find())
            return CryptoUtil.INSTANCE.decryptByAes(matcher.group(1), ProjectConstants.propertyAesKey)

        // overwrite properties here

        return value
    }

    private fun addNewProperties(): Map<String, Any> {
        val newProperties = mutableMapOf<String, Any>()

        // add new properties here

        return newProperties
    }
}
