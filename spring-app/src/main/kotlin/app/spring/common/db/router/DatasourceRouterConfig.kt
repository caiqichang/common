package app.spring.common.db.router

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Conditional
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.stereotype.Component

private const val propertyKey = "multi-datasource";

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
}

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class DB(
    val key: DataSourceKey,
)

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

@ConditionalOnProperty(name = [propertyKey])
@Configuration
class DataSourceConfig(
    private val dataSourceProp: DataSourceProp,
) {
    @Bean
    fun routingDatasource(): AbstractRoutingDataSource {
        val routingDatasource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any? {
                return DataSourceRouter.INSTANCE.getCurrentDatasource()?.name
            }
        }
        val dataSources = mutableMapOf<Any?, Any?>()
        var hasDefault = false
        dataSourceProp.routes.forEach {
            if (it.active) {
                dataSources[it.key] = DataSourceBuilder.create()
                    .url(it.url)
                    .username(it.username)
                    .password(it.password)
                    .build()
                if (it.isDefault) {
                    if (hasDefault) throw Exception("only one datasource can be set as default")
                    hasDefault = true
                    DataSourceRouter.INSTANCE.defaultKey = DataSourceKey.valueOf(it.key)
                }
            }
        }
        if (!hasDefault) throw Exception("default datasource is required")
        routingDatasource.setTargetDataSources(dataSources)
        return routingDatasource
    }
}