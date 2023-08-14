package com.taskmanagement.task.event.stream

object TaskStreamEventMeta {
    object TypeId {
        private const val TASK_V1 = "TASK_V1"

        const val header: String = "__TypeId__"

        val typeByValue: Map<String, Class<*>> = mapOf(
            TASK_V1 to TaskStreamEventV1::class.java,
        )
    }
}
