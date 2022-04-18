package app.spring.util

import ch.qos.logback.core.util.TimeUtil
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit

enum class RestTemplateUtil {
    INSTANCE;

    /**
     * 自定义RestTemplate
     *
     * @param ignoreSSL 是否忽略ssl，默认为false
     * @param timeout 超时时间，单位：毫秒，小于0则使用默认值
     */
    fun custom(
        ignoreSSL: Boolean = false,
        timeout: Int = -1,
    ): RestTemplate {
        if (ignoreSSL) {
            val client = HttpClients.custom()
            client.setSSLSocketFactory(
                SSLConnectionSocketFactory(
                    SSLContextBuilder().loadTrustMaterial { _, _ -> true }.build()
                )
            )
            if (timeout > 0) client.setConnectionTimeToLive(timeout.toLong(), TimeUnit.MILLISECONDS)
            return RestTemplate(HttpComponentsClientHttpRequestFactory(
                client.build()
            ))
        }else {
            val factory = SimpleClientHttpRequestFactory()
            if (timeout > 0) {
                factory.setConnectTimeout(timeout)
                factory.setReadTimeout(timeout)
            }
            return RestTemplate(factory)
        }
    }
}