package com.taskmanagement.employee.auth.exception

interface LocalizedMessage {
    val code: String
    val args: Map<String, Any?>
    val argOrder: List<String>
}
