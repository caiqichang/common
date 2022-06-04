package app.spring.common.auth

import app.spring.common.AopOrder
import app.spring.config.data.ApiAuthRules
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class ApiAuth(
    val value: String = "false",
)

@Component
@Aspect
@Order(AopOrder.apiAuth)
class ApiAuthAspect {

    companion object {
        private const val pointcut = "@annotation(app.spring.common.auth.ApiAuth)"
    }

    private val context = StandardEvaluationContext(ApiAuthRules())
    private val parser = SpelExpressionParser()

    init {
        ApiAuthRules::class.java.methods.forEach { context.setVariable(it.name, it) }
    }

    @Before("$pointcut && @annotation(aop)")
    fun before(aop: ApiAuth) {
        val result = parser.parseExpression(aop.value).getValue(context, Boolean::class.java)
            ?: throw RuntimeException("authorization evaluation error")
        if (!result) throw RuntimeException("permission denied")
    }
}