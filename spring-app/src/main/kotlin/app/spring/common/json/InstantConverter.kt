package app.spring.common.json

import app.spring.config.data.ProjectProperties
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
    private val projectProperties: ProjectProperties,
) : JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value === null) {
            gen?.writeNull()
        } else {
            gen?.writeString(
                DateTimeFormatter.ofPattern(projectProperties.springJacksonDateformat)
                    .format(value.atZone(ZoneId.of(projectProperties.springJacksonTimezone)))
            )
        }
    }
}

@JsonComponent
class InstantDeserializer(
    private val projectProperties: ProjectProperties,
) : JsonDeserializer<Instant>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant? {
        return p?.valueAsString.let {
            ZonedDateTime.parse(it,
                DateTimeFormatter.ofPattern(projectProperties.springJacksonDateformat)
                    .withZone(ZoneId.of(projectProperties.springJacksonTimezone))
            ).toInstant()
        }
    }
}