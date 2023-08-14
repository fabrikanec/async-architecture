package com.taskmanagement.employee.auth.login

import java.time.OffsetDateTime
import java.util.UUID

data class LoginRequest(
    val base64Data: String,
    val info: Info,
) {
    data class Info(
        val id: UUID,
        val text: String,
        val dateTime: OffsetDateTime,
    )
}
