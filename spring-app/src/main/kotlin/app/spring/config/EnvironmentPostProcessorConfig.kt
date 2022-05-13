package app.spring.config

import app.spring.common.util.CryptoUtil
import app.spring.config.data.ProjectConstants
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import java.util.regex.Pattern

class EnvironmentPostProcessorConfig : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
//        val newProperties = mutableMapOf<String, Any>()

        val pattern = Pattern.compile("AES\\[(.+)\\]")
        val decrypt = decrypt@ { k: String, v: Any, map: MutableMap<String, Any> ->
            val matcher = pattern.matcher(v.toString())
            if (matcher.find()) {
                map[k] = CryptoUtil.INSTANCE.decryptByAes(matcher.group(1), ProjectConstants.propertyAesKey)
                return@decrypt true
            }else {
                map[k] = v
                return@decrypt false
            }
        }

        environment?.propertySources?.run {
            val newPropertySources = mutableMapOf<String, MapPropertySource>()
            forEach {
                if (it is MapPropertySource) {
                    val newProperties = mutableMapOf<String, Any>()
                    var mustReplace = false
                    it.source.forEach { (k, v) ->
                        if(decrypt(k, v, newProperties)) {
                            mustReplace = true
                        }
                    }
                    if (mustReplace) {
                        newPropertySources[it.name] = MapPropertySource(it.name, newProperties)
                    }
                }
            }
            newPropertySources.forEach { (k, v) ->
                replace(k, v)
            }
            // add to `First` to overwrite the original properties
//            addFirst(MapPropertySource("newProperties${Math.random()}", newProperties))
        }
    }
}