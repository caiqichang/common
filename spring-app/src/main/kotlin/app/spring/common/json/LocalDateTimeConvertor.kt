package app.spring.common.json

import app.spring.config.data.ProjectProperties
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonComponent
class LocalDateTimeSerializer(
    private val projectProperties: ProjectProperties,
) : JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value === null) {
            gen?.writeNull()
        } else {
            gen?.writeString(
                DateTimeFormatter.ofPattern(projectProperties.springJacksonDateformat).format(value)
            )
        }
    }
}

@JsonComponent
class LocalDateTimeDeserializer(
    private val projectProperties: ProjectProperties,
) : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime? {
        return p?.valueAsString.let {
            LocalDateTime.parse(it, DateTimeFormatter.ofPattern(projectProperties.springJacksonDateformat))
        }
    }
}