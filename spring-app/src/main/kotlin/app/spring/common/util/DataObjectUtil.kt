package app.spring.common.util

import com.fasterxml.jackson.databind.ObjectMapper

object DataObjectUtil {
    inline fun <reified T> copy(t: T): T {
        val om = SpringUtil.getBean(ObjectMapper::class.java) ?: ObjectMapper()
        return om.readValue(om.writeValueAsString(t), T::class.java)
    }

    inline fun <reified T> copyList(list: List<T>): List<T> {
        return list.map { copy(it) }
    }

    fun toMap(obj: Any): Map<String, Any?> {
        val om = SpringUtil.getBean(ObjectMapper::class.java) ?: ObjectMapper()
        val map = mutableMapOf<String, Any?>()
        toMapHelper(obj, om) { k, v ->
            map[k] = v
        }
        return map
    }

    fun toStringMap(obj: Any): Map<String, String?> {
        val om = SpringUtil.getBean(ObjectMapper::class.java) ?: ObjectMapper()
        val map = mutableMapOf<String, String?>()
        toMapHelper(obj, om) { k, v ->
            map[k] = v?.toString()
        }
        return map
    }

    private fun toMapHelper(obj: Any, om: ObjectMapper, cb: (String, Any?) -> Unit) {
        om.readValue(om.writeValueAsString(obj), Map::class.java)
            .forEach { (k, v) ->
                recursiveProcess(k.toString(), v, cb)
            }
    }

    private fun recursiveProcess(key: String, value: Any?, cb: (String, Any?) -> Unit) {
        if (value is LinkedHashMap<*, *>) {
            value.forEach { k, v ->
                recursiveProcess("${key}.${k}", v, cb)
            }
            return
        }
        if (value is ArrayList<*>) {
            value.forEachIndexed { index, i ->
                recursiveProcess("${key}[${index}]", i, cb)
            }
            return
        }
        cb(key, value)
    }

}