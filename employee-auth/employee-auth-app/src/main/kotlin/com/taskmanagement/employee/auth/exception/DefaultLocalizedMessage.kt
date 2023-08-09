package com.taskmanagement.employee.auth.exception

open class DefaultLocalizedMessage(
    override val code: String,
    override val args: Map<String, Any?> = emptyMap(),
    override val argOrder: List<String> = emptyList(),
) : LocalizedMessage
