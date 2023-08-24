package com.taskmanagement.accounting.payment.event.flow.kafka

import com.accounting.payment.event.flow.PaymentFlowEvent
import com.accounting.payment.event.flow.paymentFlowMessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.UUID

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    PaymentFlowEventProperties::class,
)
class KafkaPaymentFlowEventProducerConfig(
    private val properties: PaymentFlowEventProperties,
) {

    @Bean
    fun paymentFlowEventTopic() =
        TopicBuilder.name(properties.topic).build()

    @Bean
    fun paymentFlowKafkaTemplate(
        producerPaymentFlowFactory: ProducerFactory<UUID, PaymentFlowEvent>,
    ): KafkaTemplate<UUID, PaymentFlowEvent> =
        KafkaTemplate(producerPaymentFlowFactory).apply {
            messageConverter = paymentFlowMessageConverter
        }

    @Bean
    fun kafkaPaymentFlowEventSender(
        paymentFlowKafkaTemplate: KafkaTemplate<UUID, PaymentFlowEvent>,
    ): PaymentFlowEventSender =
        PaymentFlowEventSender(
            paymentFlowKafkaTemplate,
            properties,
        )
}
