package app.spring.util

import com.fasterxml.jackson.databind.ObjectMapper

enum class DataObjectUtil {
    INSTANCE;

    fun toMapHelper(obj: Any, cb: (String, Any) -> Unit, om: ObjectMapper = ObjectMapper()) {

    }

}