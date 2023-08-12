package com.taskmanagement.tasktracker.task.shuffle

import com.taskmanagement.tasktracker.employee.jpa.EmployeeRepository
import com.taskmanagement.tasktracker.task.event.flow.v1.mapper.TaskFlowEventMapper
import com.taskmanagement.tasktracker.task.jpa.TaskRepository
import com.taskmanagement.tasktracker.task.jpa.TaskStatus
import com.taskmanagement.tasktracker.task.price.PriceResolver
import com.taskmanagement.tasktracker.task.shuffle.jpa.TaskShuffleRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Service
class TaskShuffleService(
    private val taskRepository: TaskRepository,
    private val employeeRepository: EmployeeRepository,
    private val taskShuffleRepository: TaskShuffleRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val taskFlowEventMapper: TaskFlowEventMapper,
    private val priceResolver: PriceResolver,
    private val clock: Clock,
) {
    @Transactional
    fun shuffle() {
        val shuffle = taskShuffleRepository.findClosest() ?: return
        val tasks = taskRepository.findLimitedByStatusAndUpdatedLessThen(
            status = TaskStatus.ASSIGNED.name,
            date = shuffle.created,
            limit = limit,
        ).onEach {
            it.assignee = employeeRepository.findOneRandomly()
                ?: throw IllegalStateException("No one entity found")
            it.updated = clock.instant()
            with(taskFlowEventMapper) {
                applicationEventPublisher.publishEvent(it.toTaskAssignedEventV1(priceAmount = priceResolver.priceToCharge))
            }
        }
        if (tasks.isEmpty())
            taskShuffleRepository.delete(shuffle)
        else {
            taskRepository.saveAll(tasks)
            taskShuffleRepository.save(shuffle)
        }
    }

    companion object {
        const val limit = 500
    }
}
