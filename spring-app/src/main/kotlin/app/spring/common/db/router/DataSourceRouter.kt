package app.spring.common.db.router

import app.spring.config.data.DataSourceKey

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