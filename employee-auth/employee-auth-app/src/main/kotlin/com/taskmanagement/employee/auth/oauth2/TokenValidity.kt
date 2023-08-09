package com.taskmanagement.employee.auth.oauth2

import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit

data class TokenValidity(
    @DurationUnit(ChronoUnit.SECONDS)
    val access: Duration = Duration.ofSeconds(300),
    @DurationUnit(ChronoUnit.SECONDS)
    val refresh: Duration = Duration.ofSeconds(600),
)
