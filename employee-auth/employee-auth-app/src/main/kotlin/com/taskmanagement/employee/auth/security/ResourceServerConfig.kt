package com.taskmanagement.employee.auth.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

@Configuration
@EnableResourceServer
class ResourceServerConfig(
    private val tokenServices: ResourceServerTokenServices,
    @Value("\${security.oauth2.resource.id}")
    private val resourceId: String
) : ResourceServerConfigurerAdapter() {
    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(resourceId).tokenServices(tokenServices)
    }

    override fun configure(http: HttpSecurity) {
        http
            .requestMatchers()
            .and()
            .authorizeRequests()
            .antMatchers(
                "/actuator/**",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/oauth/*",
                "/register",
                "/login/data",
            ).permitAll()
            .antMatchers("/**").authenticated()
            .and()
            .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
    }
}
