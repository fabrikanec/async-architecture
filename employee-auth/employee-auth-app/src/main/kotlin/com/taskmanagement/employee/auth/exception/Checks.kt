package com.taskmanagement.employee.auth.exception

object Checks {

    fun <T : Any> checkNotNull(value: T?, ex: () -> ProcessingException): T =
        value ?: throw ex()
}
