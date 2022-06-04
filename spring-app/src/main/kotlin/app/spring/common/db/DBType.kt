package app.spring.common.db

enum class DBType(
    /**
     * name in jdbc url
     */
    val jdbcName: String,
) {
    MySQL("mysql"),
    SQLServer("sqlserver"),
    Postgresql("postgresql");

    companion object {
        fun fromJdbcName(jdbcName: String): DBType? {
            return run scope@ {
                DBType.values().forEach {
                    if (it.jdbcName == jdbcName) return@scope it
                }
                return null
            }
        }
    }
}