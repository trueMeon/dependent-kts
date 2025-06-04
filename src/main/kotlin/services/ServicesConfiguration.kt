package org.example.dependent.kts.services

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ServicesConfiguration {
    @Bean
    fun clock(): Clock {
        return Clock.systemUTC()
    }
}