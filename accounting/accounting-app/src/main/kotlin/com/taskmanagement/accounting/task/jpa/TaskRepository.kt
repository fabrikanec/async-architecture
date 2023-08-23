package com.taskmanagement.accounting.task.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
interface TaskRepository :
    JpaRepository<Task, UUID> {

    @Query(
        """
            select max(price_to_pay) from task
            where created > :fromDate
            group by price_to_pay
        """,
        nativeQuery = true,
    )
    fun findMostExpensiveTask(fromDate: Instant): Task?

    companion object {

        fun TaskRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Task with id = [$id] not found")
    }
}
