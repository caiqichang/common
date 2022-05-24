package app.spring.common.db.router

import app.spring.config.data.DataSourceKey
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

@ConditionalOnProperty(prefix = DataSourceRouterProp.propertyKey, name = [DataSourceRouterProp.enable], havingValue = "true")
@Configuration
class DataSourceRouterConfig(
    private val dataSourceRouterProp: DataSourceRouterProp,
) {

    companion object {
        private val log = LoggerFactory.getLogger(DataSourceRouterConfig::class.java)
    }

    @Bean
    fun routingDatasource(): AbstractRoutingDataSource {
        if (dataSourceRouterProp.routes.isEmpty()) throw Exception("no datasource found")

        val routingDatasource = object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey(): Any? {
                return DataSourceRouter.INSTANCE.getCurrentDatasource()?.name
            }
        }

        val dataSources = mutableMapOf<Any?, Any?>()
        dataSourceRouterProp.routes.forEach {
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
            DataSourceRouter.INSTANCE.defaultKey = DataSourceKey.valueOf(dataSourceRouterProp.routes.first().key)
        }

        routingDatasource.setTargetDataSources(dataSources)
        return routingDatasource
    }
}