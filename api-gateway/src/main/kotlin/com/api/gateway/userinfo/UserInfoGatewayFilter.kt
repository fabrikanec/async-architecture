package com.api.gateway.userinfo

import com.api.gateway.config.properties.EmployeeAuthServiceConfigurationProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.employee.auth.user.UserApiDto
import com.user.http.headers.UserHttpHeaders
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchangeOrNull
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class UserInfoGatewayFilterFactory(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper,
    employeeAuthServiceConfigurationProperties: EmployeeAuthServiceConfigurationProperties,
) {
    private val userinfoUrl = "${employeeAuthServiceConfigurationProperties.url}/users/info"

    fun GatewayFilterSpec.create(validRoles: Set<EmployeeRole>? = null): GatewayFilterSpec =
        filter(
            UserInfoGatewayFilter(
                webClient = webClient,
                objectMapper = objectMapper,
                userinfoUrl = userinfoUrl,
                validRoles = validRoles
            )
        )
}

class UserInfoGatewayFilter(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper,
    private val userinfoUrl: String,
    private val validRoles: Set<EmployeeRole>?,
) : GatewayFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> = mono {
        webClient.get()
            .uri(userinfoUrl)
            .header(HttpHeaders.AUTHORIZATION, exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull())
            .awaitExchangeOrNull { response: ClientResponse ->
                when (response.statusCode()) {
                    HttpStatus.OK -> {
                        val bytes: ByteArray = response.awaitBody()
                        val user: UserApiDto = objectMapper.readValue(bytes)
                        if (validRoles != null && !user.roles.any { userRole -> userRole in validRoles }) {
                            exchange.response.statusCode = HttpStatus.FORBIDDEN
                            exchange.response.setComplete().awaitSingleOrNull()
                        } else {
                            chain.filter(exchange.mutateRequestWithUserHeaders(user))
                                .awaitSingleOrNull()
                        }
                    }

                    else -> {
                        exchange.response.statusCode = response.statusCode()
                        exchange.response.setComplete().awaitSingleOrNull()
                    }
                }
            }
    }

    private fun ServerWebExchange.mutateRequestWithUserHeaders(user: UserApiDto) =
        mutate().request(
            request.mutate().headers { httpHeaders ->
                httpHeaders.set(
                    UserHttpHeaders.id,
                    requireNotNull(user.id?.toString()) { "User id is null in token" },
                )
                httpHeaders.addAll(
                    UserHttpHeaders.roles,
                    user.roles.map { it.name },
                )
            }.build()
        ).build()
}
