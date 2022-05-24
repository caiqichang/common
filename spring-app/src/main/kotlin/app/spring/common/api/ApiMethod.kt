package app.spring.common.api

import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

/**
 * fill the path to method-level [RequestMapping] by method name automatically.
 * compatible with [RequestMapping]
 * @see [ApiRequestMappingHandlerMapping]
 * e.g. list() -> /list
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
@RequestMapping
@ResponseBody
annotation class ApiMethod(
    @get:AliasFor("value", annotation = RequestMapping::class)
    val value: Array<String> = [],

    @get:AliasFor("name", annotation = RequestMapping::class)
    val name: String = "",

    @get:AliasFor("path", annotation = RequestMapping::class)
    val path: Array<String> = [],

    @get:AliasFor("method", annotation = RequestMapping::class)
    val method: Array<RequestMethod> = [],

    @get:AliasFor("params", annotation = RequestMapping::class)
    val params: Array<String> = [],

    @get:AliasFor("headers", annotation = RequestMapping::class)
    val headers: Array<String> = [],

    @get:AliasFor("consumes", annotation = RequestMapping::class)
    val consumes: Array<String> = [],

    @get:AliasFor("produces", annotation = RequestMapping::class)
    val produces: Array<String> = [],
)