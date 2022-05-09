package app.spring.common.util

import org.springframework.http.MediaType
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.net.HttpURLConnection
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

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
        val factory = if (ignoreSSL) {
            object : SimpleClientHttpRequestFactory() {
                override fun prepareConnection(connection: HttpURLConnection, httpMethod: String) {
                    if (connection is HttpsURLConnection) {
                        // disable hostname verification
                        connection.setHostnameVerifier { _, _ -> true }

                        // disable certification checking
                        val sslContext = SSLContext.getInstance("SSL")
                        sslContext.init(null, Array(1) {
                            object : X509TrustManager {
                                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                                }

                                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                                }

                                override fun getAcceptedIssuers(): Array<X509Certificate>? {
                                    return null
                                }
                            }
                        }, null)
                        connection.sslSocketFactory = sslContext.socketFactory
                    }
                    super.prepareConnection(connection, httpMethod)
                }
            }
        } else {
            SimpleClientHttpRequestFactory()
        }

        if (timeout >= 0) {
            factory.setConnectTimeout(timeout)
            factory.setReadTimeout(timeout)
        }
        return RestTemplate(factory)
    }

    /**
     * resolve response body of text/plain as json
     */
    fun convertPlainToJson(restTemplate: RestTemplate): RestTemplate {
        restTemplate.messageConverters.forEach {
            if (it is MappingJackson2HttpMessageConverter) {
                it.supportedMediaTypes = listOf(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON)
            }
        }
        return restTemplate
    }
}