package app.spring.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * 自动填充接口类的RequestMapping路径
 *
 * e.g. TestController -> /test
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
@RequestMapping
@Controller
annotation class ApiController(
    @get:AliasFor("value", annotation = RequestMapping::class)
    val value: Array<String> = [],

    @get:AliasFor("value", annotation = Controller::class)
    val controllerName: String = "",

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
