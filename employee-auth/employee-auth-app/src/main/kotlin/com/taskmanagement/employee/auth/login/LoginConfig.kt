package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.login.hash.DataHasher
import com.taskmanagement.employee.auth.login.hash.Sha512DataHasher
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    LoginDataProperties::class,
)
class LoginConfig(
    private val loginDataRepository: LoginDataRepository,
    private val loginDataProperties: LoginDataProperties,
) {
    @Bean
    fun loginDataService(
        dataHasher: DataHasher,
    ): LoginDataService =
        LoginDataService(
            loginDataRepository = loginDataRepository,
            dataHasher = dataHasher,
            ttl = loginDataProperties.ttl,
            requestText = loginDataProperties.request.text,
        )

    @Bean
    fun dataHasher(): DataHasher =
        Sha512DataHasher
}
