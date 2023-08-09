package com.taskmanagement.employee.auth.login.entity

import java.time.OffsetDateTime
import java.util.UUID

data class RequestData(
    val id: UUID,
    val text: String,
    val dateTime: OffsetDateTime,
)
