package com.taskmanagement.employee.auth.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.taskmanagement.employee.service.EmployeeService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.token.TokenEnhancer

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    ClientKeyStoreProperties::class,
)
class JwtConfig(
    private val clientKeyStoreProperties: ClientKeyStoreProperties,
    private val employeeService: EmployeeService,
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun employeeTokenEnhancer(): TokenEnhancer =
        EmployeeTokenEnhancer(
            employeeService,
            objectMapper,
        )

    @Bean
    fun keyPairProvider(): KeyPairProvider =
        KeyPairProvider(clientKeyStoreProperties.toClientKeyStore())

    private fun ClientKeyStoreProperties.toClientKeyStore(): KeyPairProvider.ClientKeyStore =
        KeyPairProvider.ClientKeyStore(
            path = path,
            password = password,
            alias = alias,
        )

    @Bean
    fun jwtTokenGranterFactory(): JwtTokenGranterFactory =
        JwtTokenGranterFactory(
            employeeService,
        )
}
