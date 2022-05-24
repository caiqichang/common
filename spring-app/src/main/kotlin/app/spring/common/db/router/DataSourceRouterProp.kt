package app.spring.common.db.router

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = DataSourceRouterProp.propertyKey)
class DataSourceRouterProp {
    companion object {
        const val propertyKey = "multi-datasource"
        const val enable = "enable"
    }

    val routes: List<DataSourceRoute> = mutableListOf()
}

class DataSourceRoute {
    var key: String = ""
    var url: String = ""
    var username: String? = null
    var password: String? = null
    var active = true
    var isDefault = false
}