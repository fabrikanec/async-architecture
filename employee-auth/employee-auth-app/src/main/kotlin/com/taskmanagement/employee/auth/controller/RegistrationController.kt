package com.taskmanagement.employee.auth.controller

import com.taskmanagement.employee.jpa.Employee
import com.taskmanagement.employee.service.EmployeeService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/employees")
class RegistrationController(
    private val employeeService: EmployeeService,
) {

    @PostMapping
    fun register(@RequestBody employeeRegistrationRequest: EmployeeRegistrationRequest): Employee =
        employeeService.register(
            login = employeeRegistrationRequest.username,
            password = employeeRegistrationRequest.password,
        )
}
