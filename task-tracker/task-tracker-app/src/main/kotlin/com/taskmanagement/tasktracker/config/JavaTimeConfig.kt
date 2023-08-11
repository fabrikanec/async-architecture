package com.taskmanagement.tasktracker.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration(proxyBeanMethods = false)
class JavaTimeConfig {
    @Bean
    fun clock(): Clock =
        Clock.systemDefaultZone()
}
