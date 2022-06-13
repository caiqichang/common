package app.spring.config.data

interface AutoIncrementId {

    companion object {
        const val tableName = "auto_inc_id"
        const val pkColumnName = "id_key"
        const val valueColumnName = "id_value"
    }

}