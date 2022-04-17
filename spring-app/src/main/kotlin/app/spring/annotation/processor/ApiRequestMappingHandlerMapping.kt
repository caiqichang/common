package app.spring.annotation.processor

import app.spring.annotation.ApiController
import app.spring.annotation.ApiMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfo
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method

/**
 * 自动填充RequestMapping路径的处理类
 */
class ApiRequestMappingHandlerMapping : RequestMappingHandlerMapping() {
    override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
        val mappingInfo =  super.getMappingForMethod(method, handlerType)

        if (mappingInfo !== null) {
            // 有ApiController注解且没有设置路径才进行修改
            val apiController = handlerType.getAnnotation(ApiController::class.java)
            if (apiController !== null && apiController.value.isEmpty() && apiController.path.isEmpty()) {
                val controllerName = handlerType.simpleName
                // 首字母改为小写
                val path = "/${controllerName.lowercase()[0]}${controllerName.substring(1, (
                        if (controllerName.matches(Regex(".+Controller")))
                            controllerName.length - 10
                        else
                            // 后缀不是Controller则使用原类名
                            controllerName.length
                        ))}"

                // 有ApiMethod注解且没有设置路径才进行修改
                val apiMethod = method.getAnnotation(ApiMethod::class.java)
                if (apiMethod !== null && apiMethod.path.isEmpty() && apiMethod.value.isEmpty()) {
                    return mappingInfo.mutate().paths("${path}/${method.name}").build()
                }

                return mappingInfo.mutate().paths(
                    *mappingInfo.patternValues.map { "${path}${it}" }.toTypedArray()
                ).build()
            }
            else {
                // 有ApiMethod注解且没有设置路径才进行修改
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