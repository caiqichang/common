package app.spring.common.api

import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

/**
 * implement of [ApiMethod] and [ApiController]
 */
class ApiRequestMappingHandlerMapping : RequestMappingHandlerMapping() {
    override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
        val mappingInfo = super.getMappingForMethod(method, handlerType)

        if (mappingInfo !== null) {
            val apiController = handlerType.getAnnotation(ApiController::class.java)
            if (apiController !== null && apiController.value.isEmpty() && apiController.path.isEmpty()) {
                val controllerName = handlerType.simpleName
                // set first letter to lower case
                val path = "/${controllerName.lowercase().first()}${
                    controllerName.substring(
                        1, (
                                if (controllerName.matches(Regex(".+Controller")))
                                    controllerName.length - 10
                                else
                                // if is not end with `Controller`, use the original class name
                                    controllerName.length
                                )
                    )
                }"

                val apiMethod = method.getAnnotation(ApiMethod::class.java)
                if (apiMethod !== null && apiMethod.path.isEmpty() && apiMethod.value.isEmpty()) {
                    return mappingInfo.mutate().paths("${path}/${method.name}").build()
                }

                return mappingInfo.mutate().paths(
                    *mappingInfo.patternValues.map { "${path}${it}" }.toTypedArray()
                ).build()
            } else {
                val apiMethod = method.getAnnotation(ApiMethod::class.java)
                if (apiMethod !== null && apiMethod.path.isEmpty() && apiMethod.value.isEmpty()) {
                    return mappingInfo.mutate().paths(
                        *mappingInfo.patternValues.map { "${it}/${method.name}" }.toTypedArray()
                    ).build()
                }
            }
        }

        return mappingInfo
    }
}