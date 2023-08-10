package com.api.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiGateWayApp

fun main(args: Array<String>) {
    runApplication<ApiGateWayApp>(*args)
}
