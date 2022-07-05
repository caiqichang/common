package app.spring.common.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.DecoderHttpMessageReader
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

object WebClientUtil {

    private val defaultObjectMapper = ObjectMapper()

    init {
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    /**
     * @param convertTextAsJson resolve response body of text/plain as json if true
     * @param ignoreSSL trust all HTTPS request if true
     */
    fun config(
        convertTextAsJson: Boolean = false,
        ignoreSSL: Boolean = false,
    ): WebClient {
        val builder = WebClient.builder()

        if (convertTextAsJson) {
            builder.codecs { config ->
                var objectMapper = defaultObjectMapper
                config.readers.forEach { it ->
                    if (MediaType.APPLICATION_JSON in it.readableMediaTypes) {
                        if (it is DecoderHttpMessageReader && it.decoder is Jackson2JsonDecoder) {
                            objectMapper = (it.decoder as Jackson2JsonDecoder).objectMapper
                        }
                    }
                }

                config.customCodecs().registerWithDefaultConfig(Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN))
            }
        }

        if (ignoreSSL) {
            builder.clientConnector(ReactorClientHttpConnector(
                HttpClient.create()
                    .secure {
                        it.sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build())
                    }
            ))
        }

        return builder.build()
    }
}