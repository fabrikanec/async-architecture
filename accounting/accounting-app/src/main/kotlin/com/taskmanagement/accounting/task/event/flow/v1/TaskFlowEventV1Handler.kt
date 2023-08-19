package com.taskmanagement.accounting.task.event.flow.v1

import com.taskmanagement.accounting.task.jpa.Task
import com.taskmanagement.accounting.task.jpa.TaskRepository
import com.taskmanagement.task.event.flow.TaskAddedEventV1
import com.taskmanagement.task.event.flow.TaskCompletedEventV1
import com.taskmanagement.task.event.flow.TaskReshuffledEventV1
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TaskFlowEventV1Handler(
    private val taskRepository: TaskRepository,
    private val taskFlowEventMapper: TaskFlowEventMapper,
) {

    @Transactional
    @EventListener
    fun handle(event: TaskAddedEventV1) {
        val existingTask: Task? = taskRepository.findByIdOrNull(event.id)
        if (existingTask == null || existingTask.updated < event.updated)
            with(taskFlowEventMapper) {
                taskRepository.save(
                    event.toEntity()
                )
            }
    }

    @Transactional
    @EventListener
    fun handle(event: TaskReshuffledEventV1) {
        val existingTask: Task? = taskRepository.findByIdOrNull(event.id)
        if (existingTask == null || existingTask.updated < event.updated)
            with(taskFlowEventMapper) {
                taskRepository.save(
                    event.toEntity()
                )
            }
    }

    @Transactional
    @EventListener
    fun handle(event: TaskCompletedEventV1) {
        val existingTask: Task? = taskRepository.findByIdOrNull(event.id)
        if (existingTask == null || existingTask.updated < event.updated)
            with(taskFlowEventMapper) {
                taskRepository.save(
                    event.toEntity()
                )
            }
    }
}
