package com.taskmanagement.tasktracker.task.event.kafka

import com.taskmanagement.task.event.flow.TaskFlowEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.UUID

class TaskFlowEventSender(
    private val kafkaTemplate: KafkaTemplate<UUID, TaskFlowEvent>,
    private val properties: TaskFlowEventProperties,
) {

    @EventListener
    fun send(event: TaskFlowEvent) {
        kafkaTemplate.send(
            TaskFlowEventMessage(
                event = event,
                topic = properties.topic,
            )
        )
    }

    class TaskFlowEventMessage(
        private val event: TaskFlowEvent,
        private val topic: String,
    ) : Message<TaskFlowEvent> {

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
