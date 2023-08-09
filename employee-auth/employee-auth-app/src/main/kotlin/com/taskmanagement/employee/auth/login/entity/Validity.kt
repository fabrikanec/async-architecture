package com.taskmanagement.employee.auth.login.entity

import java.time.OffsetDateTime
import javax.persistence.Embeddable

@Embeddable
data class Validity(
    val from: OffsetDateTime,
    val until: OffsetDateTime,
)
