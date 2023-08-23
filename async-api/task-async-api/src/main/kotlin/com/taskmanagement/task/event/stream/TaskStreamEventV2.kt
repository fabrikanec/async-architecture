package com.taskmanagement.task.event.stream

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class TaskStreamEventV2(
    val id: UUID,
    val status: String,
    val description: String,
    val assigneeId: UUID,
    val created: Instant,
    val updated: Instant,
    val priceToCharge: BigDecimal,
    val priceToPay: BigDecimal,
    val jiraId: String,
) : TaskStreamEvent
