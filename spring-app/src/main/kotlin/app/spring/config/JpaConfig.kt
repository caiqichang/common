package app.spring.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.stereotype.Component
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig

@Component
class JpaAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        // get creator or updater here
        return Optional.of("unknown")
    }

}
