package com.taskmanagement.tasktracker.employee.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
interface EmployeeRepository :
    JpaRepository<Employee, UUID> {

    @Query(
        """
            select * from employee
            ORDER BY random()
            LIMIT 1;
        """,
        nativeQuery = true
    )
    fun findOneRandomly(): Employee?

    companion object {

        fun EmployeeRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")
    }
}
