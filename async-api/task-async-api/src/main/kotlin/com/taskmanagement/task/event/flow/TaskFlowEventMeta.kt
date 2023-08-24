package com.taskmanagement.task.event.flow

object TaskFlowEventMeta {
    object TypeId {
        private const val TASK_ASSIGNED_V1 = "TASK_ASSIGNED_V1"

        const val header: String = "__TypeId__"

        val typeByValue: Map<String, Class<*>> = mapOf(
            TASK_ASSIGNED_V1 to TaskReshuffledEventV1::class.java,
        )
    }
}
