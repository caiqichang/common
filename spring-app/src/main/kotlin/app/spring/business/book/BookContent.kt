package app.spring.business.book

import com.fasterxml.jackson.databind.ObjectMapper
import javax.persistence.AttributeConverter
import javax.persistence.Converter

class BookContent {

    var title: String? = null
    var isbn: String? = null

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

@Converter(autoApply = true)
class BookContentConverter : AttributeConverter<BookContent, String> {
    override fun convertToDatabaseColumn(attribute: BookContent?): String? {
        if (attribute == null) return null
        return ObjectMapper().writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): BookContent? {
        if (dbData == null) return null
        return ObjectMapper().readValue(dbData, BookContent::class.java)
    }

}