package app.spring.common.db.router

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.stereotype.Component

private const val propertyKey = "multi-datasource"

enum class DataSourceRouter {
    INSTANCE;

    private val currentDatasource = ThreadLocal<DataSourceKey>()

    var defaultKey: DataSourceKey? = null

    fun setCurrentDatasource(key: DataSourceKey) {
        currentDatasource.set(key)
    }

    fun getCurrentDatasource(): DataSourceKey? {
        return currentDatasource.get() ?: defaultKey
    }

    fun reset() {
        currentDatasource.set(defaultKey)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class DB(
    val key: DataSourceKey,
)

@Component
@Aspect
class DBAspect {
    @Pointcut("@annotation(app.spring.common.db.router.DB)")
    fun pointcut() {}

    @Before("pointcut() && @annotation(aop)")
    fun before(aop: DB) {
        DataSourceRouter.INSTANCE.setCurrentDatasource(aop.key)
    }

    @AfterReturning("pointcut()")
    fun afterReturning() {
        DataSourceRouter.INSTANCE.reset()
    }
}

class DataSourceRoute {
    var key: String = ""
    var url: String = ""
    var username: String? = null
    var password: String? = null
    var active = true
    var isDefault = false
}

@Component
@ConfigurationProperties(prefix = propertyKey)
class DataSourceProp {
    val routes: List<DataSourceRoute> = mutableListOf()
}

@ConditionalOnProperty(prefix = propertyKey, name = ["enable"], havingValue = "true")
@Configuration
class DataSourceConfig(
    private val dataSourceProp: DataSourceProp,
) {

    companion object {
        private val log = LoggerFactory.getLogger(DataSourceConfig::class.java)
    }

    @Bean
    fun routingDatasource(): AbstractRoutingDataSource {
        if (dataSourceProp.routes.isEmpty()) throw Exception("no datasource found")

        val routingDatasource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any? {
                return DataSourceRouter.INSTANCE.getCurrentDatasource()?.name
            }
        }

        val dataSources = mutableMapOf<Any?, Any?>()
        dataSourceProp.routes.forEach {
            if (it.active) {
                dataSources[it.key] = DataSourceBuilder.create()
                    .url(it.url)
                    .username(it.username)
                    .password(it.password)
                    .build()
                if (it.isDefault) {
                    if (DataSourceRouter.INSTANCE.defaultKey != null) log.warn("multiple datasource are set to default, the last one will be accepted")
                    DataSourceRouter.INSTANCE.defaultKey = DataSourceKey.valueOf(it.key)
                }
            }
        }

        if (DataSourceRouter.INSTANCE.defaultKey == null) {
            log.warn("no dataSource is set to default, the first one will be accepted")
            DataSourceRouter.INSTANCE.defaultKey = DataSourceKey.valueOf(dataSourceProp.routes.first().key)
        }

        routingDatasource.setTargetDataSources(dataSources)
        return routingDatasource
    }
}