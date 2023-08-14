package com.taskmanagement.tasktracker.task.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID
import javax.persistence.EntityNotFoundException
import javax.persistence.LockModeType

@Repository
interface TaskRepository :
    JpaRepository<Task, UUID>, PagingAndSortingRepository<Task, UUID> {

    @Query(
        """
            from Task task
            where task.id = :id
        """
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: UUID): Task?

    fun findAllByAssigneeId(assigneeId: UUID, pageable: Pageable): Page<Task>

    @Query(
        """
            select * from task
            where status = :status
            and updated <= :date
            limit :limit
        """,
        nativeQuery = true
    )
    fun findLimitedByStatusAndUpdatedLessThen(limit: Int, status: String, date: Instant): List<Task>

    companion object {

        fun TaskRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")

        fun TaskRepository.getByIdWithLockOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")
    }
}
