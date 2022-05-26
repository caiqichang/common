package app.spring.common.auth

import app.spring.common.AopOrder
import app.spring.config.data.ApiAuthRules
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.SimpleEvaluationContext
import org.springframework.stereotype.Component
import kotlin.math.log

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class ApiAuth(
    val value: String = "",
)

@Component
@Aspect
@Order(AopOrder.apiAuth)
class ApiAuthAspect {

    @Pointcut("@annotation(app.spring.common.auth.ApiAuth)")
    fun pointcut() {
    }

    private val context = SimpleEvaluationContext.forReadOnlyDataBinding().build()
    private val parser = SpelExpressionParser()

    init {
        ApiAuthRules.getRules().forEach { (k, v) ->
            context.setVariable(k, v)
        }
    }

    @Before("pointcut() && @annotation(aop)")
    fun before(aop: ApiAuth) {
        val result = parser.parseExpression(aop.value).getValue(context, Boolean::class.java)
            ?: throw RuntimeException("authorization evaluation error")
        if (!result) throw RuntimeException("permission denied")
    }
}