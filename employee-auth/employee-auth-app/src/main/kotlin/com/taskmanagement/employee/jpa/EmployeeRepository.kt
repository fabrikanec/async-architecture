package com.taskmanagement.employee.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Repository
interface EmployeeRepository :
    JpaRepository<Employee, UUID>,
    JpaSpecificationExecutor<Employee> {

    fun findByLoginAndPassword(login: String, password: String): Employee?
    companion object {

        fun EmployeeRepository.getByIdOrThrow(id: UUID) =
            findByIdOrNull(id) ?: throw EntityNotFoundException("Employee with id = [$id] not found")

        fun EmployeeRepository.getByAuthOrThrow(login: String, password: String) =
            findByLoginAndPassword(login, password) ?: throw EntityNotFoundException("Employee with login $login not found")
    }
}
