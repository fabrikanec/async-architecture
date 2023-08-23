package com.taskmanagement.accounting.employee.jpa

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
            select count(*) from employee 
            where balance < 0
            """,
        nativeQuery = true,
    )
    fun negativeBalanceCount(): Int

    companion object {
        fun EmployeeRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")
    }
}
