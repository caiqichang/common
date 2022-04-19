package app.spring.json

import app.spring.config.ApplicationProperties
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonComponent
class InstantSerializer(
    private val applicationProperties: ApplicationProperties,
) : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value === null) {
            gen?.writeNull()
        } else {
            gen?.writeString(
                DateTimeFormatter.ofPattern(applicationProperties.springJacksonDateformat)
                    .format(value.atZone(ZoneId.of(applicationProperties.springJacksonTimezone)))
            )
        }
    }
}

@JsonComponent
class InstantDeserializer(
    private val applicationProperties: ApplicationProperties,
) : JsonDeserializer<Instant>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant? {
        return p?.valueAsString.let {
            ZonedDateTime.parse(it,
                DateTimeFormatter.ofPattern(applicationProperties.springJacksonDateformat)
                    .withZone(ZoneId.of(applicationProperties.springJacksonTimezone))
            ).toInstant()
        }
    }
}