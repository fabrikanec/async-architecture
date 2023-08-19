package com.taskmanagement.accounting.task.event.stream

import com.taskmanagement.task.event.stream.TaskStreamEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.transaction.annotation.Transactional

open class TaskStreamEventListener(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    @KafkaListener(
        topics = [taskStreamKafkaSpel],
        containerFactory = taskStreamKafkaListenerContainerFactoryName,
    )
    open fun consume(event: TaskStreamEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
