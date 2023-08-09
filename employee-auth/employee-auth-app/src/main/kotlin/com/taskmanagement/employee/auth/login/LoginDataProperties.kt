package com.taskmanagement.employee.auth.login

import com.taskmanagement.employee.auth.util.EmployeeAuthServiceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit

@ConfigurationProperties("${EmployeeAuthServiceProperties.KEY}.login.data")
@ConstructorBinding
data class LoginDataProperties(
    @DurationUnit(ChronoUnit.SECONDS)
    val ttl: Duration = Duration.ofMinutes(5),
    val request: Request = Request(),
) {
    data class Request(
        val text: String = "Запрос на подключение",
    )
}
