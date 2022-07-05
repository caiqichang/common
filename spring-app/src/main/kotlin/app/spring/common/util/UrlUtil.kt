package app.spring.common.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object UrlUtil {

    /**
     * Encode URL query parameters
     */
    fun encode(raw: String): String {
        return URLEncoder.encode(raw, StandardCharsets.UTF_8).replace("+", "%20")
    }
}