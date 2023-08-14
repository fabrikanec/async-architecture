package com.api.gateway.tasktracker

import com.api.gateway.config.properties.TaskTrackerServiceConfigurationProperties
import com.api.gateway.userinfo.UserInfoGatewayFilterFactory
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
    TaskTrackerServiceConfigurationProperties::class,
)
class TaskTrackerRouteConfig(
    val taskTrackerServiceConfigurationProperties: TaskTrackerServiceConfigurationProperties,
    val userInfoGatewayFilterFactory: UserInfoGatewayFilterFactory,
) {
    @Bean
    fun taskTrackerRouteLocator(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes {
            route(id = "taskTrackerMapping") {
                path("/tasks/**") and method(GET, POST)
                filters {
                    with(userInfoGatewayFilterFactory) {
                        create()
                    }
                }
                uri(taskTrackerServiceConfigurationProperties.url)
            }
        }
}
