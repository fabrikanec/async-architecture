package com.taskmanagement.tasktracker.task.service

import com.taskmanagement.employee.EmployeeRole
import com.taskmanagement.tasktracker.AddTaskRequest
import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import com.taskmanagement.tasktracker.task.event.flow.v1.mapper.TaskFlowEventMapper
import com.taskmanagement.tasktracker.task.event.stream.v1.mapper.TaskStreamEventMapper
import com.taskmanagement.tasktracker.task.jpa.Task
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import com.taskmanagement.tasktracker.task.jpa.TaskRepository.Companion.getByIdOrThrow
import com.taskmanagement.tasktracker.task.jpa.TaskStatus
import com.taskmanagement.tasktracker.task.price.PriceResolver
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffle
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffleRepository
import com.user.User
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.util.UUID

@Service
class TaskTrackerService(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskFlowEventMapper: TaskFlowEventMapper,
    private val taskStreamEventMapper: TaskStreamEventMapper,
    private val taskShuffleRepository: TaskShuffleRepository,
    private val priceResolver: PriceResolver,
    private val clock: Clock,
) {
    @Transactional
    fun add(addTaskRequest: AddTaskRequest): Task {
        val assignee = employeeRepository.getByIdOrThrow(addTaskRequest.assignee)
        val task = Task(
            created = clock.instant(),
            assignee = assignee,
            description = addTaskRequest.description,
            priceToCharge = priceResolver.priceToCharge,
            priceToPay = priceResolver.priceToPay,
            title = addTaskRequest.jiraId,
        )
        taskRepository.save(task)

        with(taskStreamEventMapper) {
            applicationEventPublisher.publishEvent(task.toStreamEventV1())
        }

        with(taskFlowEventMapper) {
            applicationEventPublisher.publishEvent(task.toTaskAddedEventV1())
        }

        return task
    }

    @Transactional
    fun complete(
        taskId: UUID,
        user: User,
    ) {
        val task = taskRepository.getByIdOrThrow(taskId)

        require(task.assignee.id == user.id) {
            "You are not assigned to this task"
        }

        taskShuffleRepository.findLatest()?.let { shuffle ->
            require(task.updated > shuffle.created) {
                "There is a shuffle in progress"
            }
        }

        task.status = TaskStatus.COMPLETED
        task.updated = clock.instant()

        taskRepository.save(task)

        with(taskStreamEventMapper) {
            applicationEventPublisher.publishEvent(task.toStreamEventV1())
        }

        with(taskFlowEventMapper) {
            applicationEventPublisher.publishEvent(task.toTaskCompletedEventV1())
        }
    }

    @Transactional
    fun shuffle(
        user: User,
    ) {
        require(user.roles.any { it in shufflePerimitedRoles }) {
            "You are not allowed to shuffle"
        }
        taskShuffleRepository.save(TaskShuffle(created = clock.instant()))
    }

    @Transactional(readOnly = true)
    fun getAll(
        user: User,
        pageable: Pageable,
    ): Page<Task> =
        taskRepository.findAllByAssigneeId(assigneeId = user.id, pageable = pageable)

    companion object {
        private val shufflePerimitedRoles = setOf(
            EmployeeRole.MANAGER.name,
            EmployeeRole.ADMINISTRATOR.name,
        )
    }
}
