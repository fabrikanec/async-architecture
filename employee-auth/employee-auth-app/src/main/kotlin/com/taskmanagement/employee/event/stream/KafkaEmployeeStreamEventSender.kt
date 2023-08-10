package com.taskmanagement.employee.event.stream

import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.UUID

class KafkaEmployeeStreamEventSender(
    private val kafkaTemplate: KafkaTemplate<UUID, EmployeeStreamEvent>,
    private val properties: KafkaEmployeeStreamEventProperties,
) {

    @EventListener
    fun send(event: EmployeeStreamEvent) {
        kafkaTemplate.send(
            EmployeeMessage(
                event = event,
                topic = properties.topic,
            )
        )
    }

    class EmployeeMessage(
        private val event: EmployeeStreamEvent,
        private val topic: String,
    ) : Message<EmployeeStreamEvent> {

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
