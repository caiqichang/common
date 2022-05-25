package app.spring.config.data

import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

class ApiAuthRules {
    companion object {
        private val log = LoggerFactory.getLogger(ApiAuthRules::class.java)

        fun getRules(): Map<String, Any> {
            val companionObject = ApiAuthRules::class.companionObject ?: return mapOf()
            return mapOf(
                "hasRoles" to companionObject::class.java.getMethod("hasRoles", Array<String>::class.java)
            )
        }

        fun hasRoles(vararg str: String): Boolean {
            log.info(str.joinToString())
            return true
        }
    }
}