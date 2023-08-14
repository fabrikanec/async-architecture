package com.taskmanagement.task.event.flow

import java.math.BigInteger
import java.time.Instant
import java.util.UUID

data class TaskCompletedEventV1(
    val id: UUID,
    val created: Instant,
    val priceAmount: BigInteger,
) : TaskFlowEvent
