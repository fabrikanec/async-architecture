package com.taskmanagement.employee.auth.oauth2

import com.fasterxml.jackson.databind.ObjectMapper
import com.taskmanagement.employee.auth.jwt.JwtTokenGranterFactory
import com.taskmanagement.employee.auth.jwt.KeyPairProvider
import org.slf4j.lazyLogger
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception
import org.springframework.security.oauth2.common.util.JsonParser
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.TokenGranter
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(
    ClientDetailsConfigurationProperties::class,
    TokenProperties::class,
)
class OAuth2ServerConfig(
    private val tokenProperties: TokenProperties,
    private val keyPairProvider: KeyPairProvider,
    private val authenticationManager: AuthenticationManager,
    private val employeeTokenEnhancer: TokenEnhancer,
    private val clientDetailsConfigurationProperties: ClientDetailsConfigurationProperties,
    private val oauth2ResponseExceptionTranslator: WebResponseExceptionTranslator<OAuth2Exception>,
    private val jwtTokenGranterFactory: JwtTokenGranterFactory,
    private val objectMapper: ObjectMapper,
) : AuthorizationServerConfigurer {

    private val log by lazyLogger(OAuth2ServerConfig::class)

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .authenticationManager(authenticationManager)
            .tokenServices(tokenServices())
            .tokenStore(tokenStore())
            .tokenEnhancer(tokenEnhancer())
            .tokenGranter(tokenGranter(endpoints))
            .accessTokenConverter(accessTokenConverter())
            .exceptionTranslator(oauth2ResponseExceptionTranslator)
    }

    private fun tokenGranter(endpoints: AuthorizationServerEndpointsConfigurer): TokenGranter =
        CompositeTokenGranter(
            listOf(endpoints.tokenGranter, jwtTokenGranterFactory.create(endpoints))
        )

    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer
            .tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_TRUSTED_CLIENT')")
            .checkTokenAccess("hasAuthority('ROLE_TRUSTED_CLIENT')")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.withClientDetails(employeeClientDetailsService())
    }

    @Bean
    fun employeeClientDetailsService(): ClientDetailsService = InMemoryClientDetailsService().apply {
        setClientDetailsStore(
            clientDetailsConfigurationProperties.clients.mapValues {
                it.value.toClientDetails(clientId = it.key)
            }
        )
    }

    private fun ClientDetailsConfigurationProperties.ClientData.toClientDetails(clientId: String): ClientDetails =
        EmployeeAuthClientDetails(
            clientId = clientId,
            clientSecret = clientSecret,
            scope = scope,
            authorizedGrantTypes = authorizedGrantTypes,
            registeredRedirectUri = registeredRedirectUri,
            resourceIds = resourceIds,
            authorities = authorities,
            accessTokenValiditySeconds = token.validity.access.seconds.toInt(),
            refreshTokenValiditySeconds = token.validity.refresh.seconds.toInt(),
        )

    @Bean
    fun tokenStore(): TokenStore = JwtTokenStore(accessTokenConverter())

    @Bean
    fun tokenEnhancer(): TokenEnhancer = TokenEnhancerChain().apply {
        setTokenEnhancers(
            listOf(employeeTokenEnhancer, accessTokenConverter())
        )
    }

    @Bean
    fun accessTokenConverter() = JwtAccessTokenConverter().apply {
        setObjectMapper(ObjectMapperJsonParser(objectMapper))
        try {
            setKeyPair(keyPairProvider.getKeyPair())
        } catch (ex: Exception) {
            log.error("Error jwt parse: " + ex.message, ex)
        }
        log.info("Jwt parse successfully done")
    }

    @Bean
    @Primary
    fun tokenServices() = DefaultTokenServices().apply {
        setTokenStore(tokenStore())
        setSupportRefreshToken(true)
        setTokenEnhancer(tokenEnhancer())
        with(tokenProperties.validity) {
            setAccessTokenValiditySeconds(access.seconds.toInt())
            setRefreshTokenValiditySeconds(refresh.seconds.toInt())
        }
    }

    companion object {
        private fun JwtAccessTokenConverter.setObjectMapper(objectMapper: JsonParser) {
            val objectMapperField = this::class.java.getDeclaredField("objectMapper")
            objectMapperField.isAccessible = true
            objectMapperField.set(this, objectMapper)
        }
    }
}
