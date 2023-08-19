package com.taskmanagement.accounting.task.event.flow

import com.taskmanagement.task.event.flow.TaskFlowEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.transaction.annotation.Transactional

open class TaskFlowEventListener(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    @KafkaListener(
        topics = [taskFlowKafkaSpel],
        containerFactory = taskFlowKafkaListenerContainerFactoryName,
    )
    open fun consume(event: TaskFlowEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
