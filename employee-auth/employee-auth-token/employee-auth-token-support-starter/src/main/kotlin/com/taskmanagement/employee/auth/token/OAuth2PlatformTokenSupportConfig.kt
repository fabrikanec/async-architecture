package com.taskmanagement.employee.auth.token

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.token.TokenStore

@Configuration
@ConditionalOnClass(TokenStore::class)
@ConditionalOnBean(TokenStore::class)
@AutoConfigureAfter(
    OAuth2AutoConfiguration::class,
)
class OAuth2PlatformTokenSupportConfig(
    private val tokenStore: TokenStore,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun companyOAuth2TokenSupport(): EmployeeOAuth2TokenSupport =
        EmployeeOAuth2TokenSupportImpl(
            tokenStore = tokenStore,
            objectMapper = objectMapper
        )
}
