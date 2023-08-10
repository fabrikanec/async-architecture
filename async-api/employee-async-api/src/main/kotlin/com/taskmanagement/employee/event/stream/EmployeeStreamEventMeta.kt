package com.taskmanagement.employee.event.stream

object EmployeeStreamEventMeta {
    object TypeId {
        private const val EMPLOYEE_V1 = "EMPLOYEE_V1"

        const val header: String = "__TypeId__"

        val typeByValue: Map<String, Class<*>> = mapOf(
            EMPLOYEE_V1 to EmployeeStreamEventV1::class.java,
        )
    }
}
