package com.taskmanagement.accounting.payment.event.flow.kafka

import com.accounting.payment.event.flow.PaymentFlowEvent
import org.springframework.context.event.EventListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.UUID

class PaymentFlowEventSender(
    private val kafkaTemplate: KafkaTemplate<UUID, PaymentFlowEvent>,
    private val properties: PaymentFlowEventProperties,
) {

    @EventListener
    fun send(event: PaymentFlowEvent) {
        kafkaTemplate.send(
            PaymentFlowEventMessage(
                event = event,
                topic = properties.topic,
            )
        )
    }

    class PaymentFlowEventMessage(
        private val event: PaymentFlowEvent,
        private val topic: String,
    ) : Message<PaymentFlowEvent> {

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
