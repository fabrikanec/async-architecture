package com.taskmanagement.accounting.task.event.flow

import com.taskmanagement.task.event.flow.TaskFlowEvent
import org.slf4j.debug
import org.slf4j.lazyLogger
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.DltHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.kafka.retrytopic.DltStrategy
import org.springframework.transaction.annotation.Transactional

open class TaskFlowEventListener(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {
    private val logger by lazyLogger(this::class)

    /**
     * dlq strategy: N non-blocking retry, after that event should be sent to dlt(dead-letter topic)
     */
    @Transactional
    @RetryableTopic(
        kafkaTemplate = "taskFlowRetryKafkaTemplate",
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
    )
    @KafkaListener(
        topics = [taskFlowKafkaSpel],
        containerFactory = taskFlowKafkaListenerContainerFactoryName,
    )
    open fun consume(event: TaskFlowEvent) {
        applicationEventPublisher.publishEvent(event)
    }

    @DltHandler
    open fun handle(event: TaskFlowEvent) {
        logger.debug { "Task flow event handled in dlt topic" }
        // handle method can resend event to source topic task-flow
    }
}
