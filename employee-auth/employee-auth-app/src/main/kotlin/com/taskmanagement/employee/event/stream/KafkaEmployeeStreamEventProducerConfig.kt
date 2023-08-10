package com.taskmanagement.employee.event.stream

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.UUID

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    KafkaEmployeeStreamEventProperties::class,
)
class KafkaEmployeeStreamEventProducerConfig(
    private val properties: KafkaEmployeeStreamEventProperties,
) {

    @Bean
    fun employeeStreamEventTopic() =
        TopicBuilder.name(properties.topic).build()

    @Bean
    fun employeeStreamKafkaTemplate(
        producerEmployeeFactory: ProducerFactory<UUID, EmployeeStreamEvent>,
    ): KafkaTemplate<UUID, EmployeeStreamEvent> =
        KafkaTemplate(producerEmployeeFactory).apply {
            messageConverter = employeeMessageConverter
        }

    @Bean
    fun kafkaEmployeeStreamEventSender(
        employeeStreamKafkaTemplate: KafkaTemplate<UUID, EmployeeStreamEvent>,
    ): KafkaEmployeeStreamEventSender =
        KafkaEmployeeStreamEventSender(
            employeeStreamKafkaTemplate,
            properties,
        )
}
