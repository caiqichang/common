package app.spring.common.db.router

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "multi-datasource")
class DatasourceRouterProp {
    val routes: List<DatasourceRoute> = mutableListOf()
}

class DatasourceRoute {
    var key: String? = null
    var url: String? = null
    var username: String? = null
    var password: String? = null
}

