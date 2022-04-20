package app.spring.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class UrlUtil {
    INSTANCE;

    /**
     * Encode URL query parameters
     */
    fun encode(raw: String): String {
        return URLEncoder.encode(raw, StandardCharsets.UTF_8).replace("\\+", "%20")
    }
}