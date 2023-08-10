package com.api.gateway

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = [
        "employee-auth-service.url=http://localhost:\${wiremock.server.port}",
        "task-tracker-service.url=http://localhost:\${wiremock.server.port}",
        "accounting-service.url=http://localhost:\${wiremock.server.port}",
    ],
)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
abstract class ApiGateWayAppContextTest {

    @LocalServerPort
    private var port = 1080

    lateinit var client: WebTestClient

    @BeforeEach
    fun setup() {
        client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }
}
