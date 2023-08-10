package com.user.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration(proxyBeanMethods = false)
open class UserWebAutoConfiguration : WebMvcConfigurer {
    @Bean
    open fun invalidUserExceptionHandler() =
        InvalidUserExceptionHandler()

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers += UserHandlerMethodArgumentResolver()
    }
}
