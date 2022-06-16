package app.spring.common.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.http.codec.DecoderHttpMessageReader
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.web.reactive.function.client.WebClient

enum class WebClientUtil {
    INSTANCE;

    private fun initDefaultObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        return objectMapper
    }

    fun custom(
        convertTextAsJson: Boolean = false,
        customObjectMapper: ObjectMapper? = null,
    ): WebClient {
        val builder = WebClient.builder()

        if (convertTextAsJson) {
            builder.codecs { config ->
                val objectMapper = if (customObjectMapper == null) {
                    var defaultObjectMapper = initDefaultObjectMapper()
                    config.readers.forEach {
                        if (it.readableMediaTypes.contains(MediaType.APPLICATION_JSON)) {
                            if (it is DecoderHttpMessageReader && it.decoder is Jackson2JsonDecoder) {
                                defaultObjectMapper = (it.decoder as Jackson2JsonDecoder).objectMapper
                            }
                        }
                    }
                    defaultObjectMapper
                } else customObjectMapper

                config.customCodecs().registerWithDefaultConfig(Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN))
            }
        }

        return builder.build()
    }
}