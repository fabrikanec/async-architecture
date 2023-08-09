package com.taskmanagement.employee.auth.login.entity

import java.util.UUID
import javax.persistence.Embeddable

@Embeddable
data class Request(
    val id: UUID,
    val hash: String,
)
