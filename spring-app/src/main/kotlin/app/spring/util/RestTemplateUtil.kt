package app.spring.util

import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.util.concurrent.TimeUnit

enum class RestTemplateUtil {
    INSTANCE;

    /**
     * @param ignoreSSL
     * @param timeout unit is milliseconds. if lower than 0, set to default
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
            if (timeout >= 0) client.setConnectionTimeToLive(timeout.toLong(), TimeUnit.MILLISECONDS)
            return RestTemplate(HttpComponentsClientHttpRequestFactory(
                client.build()
            ))
        }else {
            val factory = SimpleClientHttpRequestFactory()
            if (timeout >= 0) {
                factory.setConnectTimeout(timeout)
                factory.setReadTimeout(timeout)
            }
            return RestTemplate(factory)
        }
    }

    /**
     * resolve response body of text/plain as json
     */
    fun convertPlainToJson(restTemplate: RestTemplate): RestTemplate {
        restTemplate.messageConverters.forEach {
            if (it is MappingJackson2HttpMessageConverter) {
                it.supportedMediaTypes = listOf( MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON )
            }
        }
        return restTemplate
    }
}