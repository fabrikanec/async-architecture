package com.taskmanagement.employee

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EmployeeAuthApplication

fun main(vararg args: String) {
    SpringApplication.run(EmployeeAuthApplication::class.java, *args)
}
