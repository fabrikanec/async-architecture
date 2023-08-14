package com.taskmanagement.tasktracker.task.controller

import com.taskmanagement.tasktracker.AddTaskRequest
import com.taskmanagement.tasktracker.CompleteTaskRequest
import com.taskmanagement.tasktracker.TaskResponseDto
import com.taskmanagement.tasktracker.task.jpa.Task
import com.taskmanagement.tasktracker.task.service.TaskTrackerService
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskTrackerController(
    private val taskTrackerService: TaskTrackerService,
) {
    @PostMapping
    fun add(
        @RequestBody addTaskRequest: AddTaskRequest,
        user: User,
    ) = taskTrackerService.add(addTaskRequest)

    @PostMapping(
        "/complete"
    )
    fun complete(
        @RequestBody completeTaskRequest: CompleteTaskRequest,
        user: User,
    ) = taskTrackerService.complete(
        taskId = completeTaskRequest.taskId,
        user = user,
    )

    @PostMapping("/shuffle")
    fun shuffle(
        user: User,
    ) =
        taskTrackerService.shuffle(user = user)

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
        fun Task.toHttpDto(): TaskResponseDto =
            TaskResponseDto(
                id = id,
                created = created,
                description = description,
            )
    }
}
