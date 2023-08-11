package com.taskmanagement.tasktracker.util.schedule

import org.springframework.scheduling.support.ScheduledMethodRunnable
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

inline fun <reified T : Any> scheduledMethodRunnable(
    scheduled: T,
    kFunction: KFunction<T>
): ScheduledMethodRunnable =
    ScheduledMethodRunnable(
        scheduled,
        kFunction.javaMethod ?: error("Cannot get method for ${T::class.simpleName}")
    )
