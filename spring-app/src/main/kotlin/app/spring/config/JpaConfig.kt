package app.spring.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import javax.persistence.MappedSuperclass
import javax.persistence.Version

@EnableJpaAuditing
@Configuration
class JpaConfig

@MappedSuperclass
open class BaseEntity {

    @CreatedBy
    var createdBy: String? = null

    @LastModifiedBy
    var lastModifiedBy: String? = null

    @CreatedDate
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    var lastModifiedDate: LocalDateTime? = null

    @Version
    var version: Int? = null
}

@Component
class JpaAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        // get creator or updater here
        return Optional.of("unknown")
    }

}
