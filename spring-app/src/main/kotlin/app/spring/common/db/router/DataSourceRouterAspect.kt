package app.spring.common.db.router

import app.spring.common.AopOrder
import app.spring.config.data.DataSourceKey
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class DB(
    val key: DataSourceKey,
)

@Component
@Aspect
@Order(AopOrder.db)
class DataSourceRouterAspect {

    companion object {
        private const val pointcut = "@annotation(app.spring.common.db.router.DB)"
    }

    @Before("$pointcut && @annotation(aop)")
    fun before(aop: DB) {
        DataSourceRouter.INSTANCE.setCurrentDatasource(aop.key)
    }

    @AfterReturning(pointcut)
    fun afterReturning() {
        DataSourceRouter.INSTANCE.reset()
    }

    @AfterThrowing(pointcut)
    fun afterThrowing() {
        DataSourceRouter.INSTANCE.reset()
    }
}

