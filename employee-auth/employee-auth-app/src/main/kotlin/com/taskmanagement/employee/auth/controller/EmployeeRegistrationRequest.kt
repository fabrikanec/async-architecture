package com.taskmanagement.employee.auth.controller

data class EmployeeRegistrationRequest(
    val login: String,
    val password: String,
)
