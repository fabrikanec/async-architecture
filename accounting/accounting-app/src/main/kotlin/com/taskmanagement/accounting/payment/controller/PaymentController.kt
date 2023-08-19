package com.taskmanagement.accounting.payment.controller

import com.taskmanagement.accounting.payment.jpa.Payment
import com.taskmanagement.tasktracker.TaskResponseDto
import com.taskmanagement.tasktracker.operationhistory.service.TaskTrackerService
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payments")
class PaymentController(
    private val taskTrackerService: TaskTrackerService,
) {

    @GetMapping
    fun getAll(
        user: User,
        pageable: Pageable,
    ) =
        taskTrackerService.getAll(
            user = user,
            pageable = pageable,
        ).map { it.toHttpDto() }

    companion object {
        fun Payment.toHttpDto(): TaskResponseDto =
            TaskResponseDto(
                id = id,
                created = created,
                description = description,
            )
    }
}
