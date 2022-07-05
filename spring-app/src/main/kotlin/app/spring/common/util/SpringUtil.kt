package app.spring.common.util

import org.springframework.context.ApplicationContext

object SpringUtil {

    var applicationContext: ApplicationContext? = null

    fun <T> getBean(clazz: Class<T>): T? {
        return applicationContext?.getBean(clazz)
    }
}