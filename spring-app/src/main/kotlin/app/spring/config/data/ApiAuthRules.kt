package app.spring.config.data

import org.slf4j.LoggerFactory

class ApiAuthRules {
    companion object {
        private val log = LoggerFactory.getLogger(ApiAuthRules::class.java)

        fun getRules(): Map<String, Any?> {
            return mapOf(
                "hasRoles" to ApiAuthRules::class.java.getMethod("hasRoles", Array<String>::class.java)
            )
        }

        @JvmStatic
        fun hasRoles(vararg str: String): Boolean {
            log.info(str.joinToString())
            return true
        }
    }
}