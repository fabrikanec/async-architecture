package com.api.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EmployeeApiGateWayApp

fun main(args: Array<String>) {
    runApplication<EmployeeApiGateWayApp>(*args)
}
