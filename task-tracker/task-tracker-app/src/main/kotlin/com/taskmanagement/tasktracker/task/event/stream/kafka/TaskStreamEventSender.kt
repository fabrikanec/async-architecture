package com.taskmanagement.tasktracker.task.event.stream.kafka

import com.taskmanagement.task.event.stream.TaskStreamEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.UUID

class TaskStreamEventSender(
    private val kafkaTemplate: KafkaTemplate<UUID, TaskStreamEvent>,
    private val properties: TaskStreamEventProperties,
) {

    @EventListener
    fun send(event: TaskStreamEvent) {
        kafkaTemplate.send(
            TaskStreamEventMessage(
                event = event,
                topic = properties.topic,
            )
        )
    }

    class TaskStreamEventMessage(
        private val event: TaskStreamEvent,
        private val topic: String,
    ) : Message<TaskStreamEvent> {

        override fun getPayload() =
            event

        override fun getHeaders() =
            MessageHeaders(
                mapOf(
                    KafkaHeaders.TOPIC to topic,
                )
            )
    }
}
