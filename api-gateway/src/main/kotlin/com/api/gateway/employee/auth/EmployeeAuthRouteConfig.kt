package com.api.gateway.employee.auth

import com.api.gateway.config.properties.EmployeeAuthServiceConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    EmployeeAuthServiceConfigurationProperties::class,
)
class EmployeeAuthRouteConfig(
    val employeeAuthServiceConfigurationProperties: EmployeeAuthServiceConfigurationProperties,
) {
    @Bean
    fun employeeAuthRouteLocator(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route(id = "employeeRegister") {
                path("/employees") and method(POST)
                uri(employeeAuthServiceConfigurationProperties.url)
            }
            route(id = "employeeAuthToken") {
                val apiPath = "/token"
                path("$apiPath/**") and method(POST)
                filters {
                    rewritePath(
                        "$apiPath(.*)",
                        "/oauth/token$1"
                    )
                }
                uri(employeeAuthServiceConfigurationProperties.url)
            }
            route(id = "employeeAuthUserInfo") {
                val apiPath = "/userinfo"
                path("$apiPath/**") and method(GET)
                filters {
                    rewritePath(
                        "$apiPath(.*)",
                        "/users/info$1"
                    )
                }
                uri(employeeAuthServiceConfigurationProperties.url)
            }
        }
}
