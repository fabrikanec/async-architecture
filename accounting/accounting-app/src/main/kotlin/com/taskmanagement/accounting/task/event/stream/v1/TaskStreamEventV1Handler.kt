package com.taskmanagement.accounting.task.event.stream.v1

import com.taskmanagement.accounting.task.event.stream.v1.mapper.TaskStreamEventV1Mapper
import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskRepository
import com.taskmanagement.task.event.stream.TaskStreamEventV1
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskStreamEventV1Handler(
    private val taskRepository: TaskRepository,
    private val taskStreamEventMapper: TaskStreamEventV1Mapper,
) {

    @Transactional
    @EventListener
    fun handle(event: TaskStreamEventV1) {
        val existingTask: Task? = taskRepository.findByIdOrNull(event.id)
        if (existingTask == null || existingTask.updated < event.updated)
            with(taskStreamEventMapper) {
                taskRepository.save(
                    event.toEntity()
                )
            }
    }
}
