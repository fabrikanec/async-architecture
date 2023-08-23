package com.api.gateway.accounting

import com.api.gateway.config.properties.AccountingServiceConfigurationProperties
import com.api.gateway.userinfo.UserInfoGatewayFilterFactory
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    AccountingServiceConfigurationProperties::class,
)
class AccountingRouteConfig(
    val accountingServiceConfigurationProperties: AccountingServiceConfigurationProperties,
    val userInfoGatewayFilterFactory: UserInfoGatewayFilterFactory,
) {
    @Bean
    fun accountingRouteLocator(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route(id = "AccountingMapping") {
                path("/payments/**") and method(GET,)
                filters {
                    with(userInfoGatewayFilterFactory) {
                        create()
                    }
                }
                path("/analytics/**") and method(GET,)
                filters {
                    with(userInfoGatewayFilterFactory) {
                        create()
                    }
                }
                path("/balances/**") and method(GET,)
                filters {
                    with(userInfoGatewayFilterFactory) {
                        create()
                    }
                }
                uri(accountingServiceConfigurationProperties.url)
            }
        }
}
