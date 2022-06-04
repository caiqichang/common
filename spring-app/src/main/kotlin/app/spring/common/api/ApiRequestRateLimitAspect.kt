package app.spring.common.api

import app.spring.common.AopOrder
import app.spring.common.util.RequestUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.core.annotation.Order
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class ApiRequestRateLimit(
    /**
     * max times in period
     */
    val max: Int,
    /**
     * seconds of period
     */
    val seconds: Long,
    val group: String = "",
)

@Component
@Aspect
@Order(AopOrder.apiRequestRateLimit)
class ApiRequestRateLimitAspect {

    companion object {
        private const val pointcut = "@annotation(app.spring.common.api.ApiRequestRateLimit)"
    }

    private val buckets = ConcurrentHashMap<String, TokenBucket>()

    @Before("$pointcut && @annotation(aop)")
    fun before(joinPoint: JoinPoint, aop: ApiRequestRateLimit) {
        var key = RequestUtil.INSTANCE.clientIp()
        key += if (aop.group == "") {
            "_${joinPoint.target.javaClass.name}#${joinPoint.signature.name}"
        } else {
            "_${aop.group}"
        }
        var bucket = buckets[key]
        if (bucket == null) {
            bucket = TokenBucket(aop.max, aop.seconds * 1000)
            bucket.getToken()
            buckets[key] = bucket
        } else {
            if (!bucket.getToken())
                throw RuntimeException("out of request rate limit (permit ${aop.max} times in ${aop.seconds} seconds)")
        }
    }

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
    fun clearBucket() {
        val current = System.currentTimeMillis()
        buckets.entries.removeIf {
            current - it.value.startTime > it.value.milliseconds
        }
    }
}

class TokenBucket(
    private val max: Int,
    val milliseconds: Long,
) {
    var startTime: Long = 0L
        private set

    private var tokenCount = 0

    fun getToken(): Boolean {
        synchronized(this) {
            val current = System.currentTimeMillis()
            if (current - startTime > milliseconds) {
                tokenCount = 1
                startTime = current
                return true
            }
            if (tokenCount < max) {
                tokenCount++
                return true
            }
            return false
        }
    }
}