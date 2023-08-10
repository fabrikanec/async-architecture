package com.taskmanagement.employee.auth

import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(
    DataSourceAutoConfiguration::class,
)
class EmployeeAuthApplicationContextTest {
    @Test
    fun contextLoads() {
    }
}
