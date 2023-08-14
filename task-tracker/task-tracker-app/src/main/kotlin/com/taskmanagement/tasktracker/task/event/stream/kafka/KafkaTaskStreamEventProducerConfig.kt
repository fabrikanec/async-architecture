package com.taskmanagement.tasktracker.task.event.stream.kafka

import com.taskmanagement.task.event.stream.TaskStreamEvent
import com.taskmanagement.task.event.stream.taskStreamMessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.UUID

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    TaskStreamEventProperties::class,
)
class KafkaTaskStreamEventProducerConfig(
    private val properties: TaskStreamEventProperties,
) {

    @Bean
    fun taskStreamEventTopic() =
        TopicBuilder.name(properties.topic).build()

    @Bean
    fun taskStreamKafkaTemplate(
        producerTaskStreamFactory: ProducerFactory<UUID, TaskStreamEvent>,
    ): KafkaTemplate<UUID, TaskStreamEvent> =
        KafkaTemplate(producerTaskStreamFactory).apply {
            messageConverter = taskStreamMessageConverter
        }

    @Bean
    fun kafkaTaskStreamEventSender(
        taskStreamKafkaTemplate: KafkaTemplate<UUID, TaskStreamEvent>,
    ): TaskStreamEventSender =
        TaskStreamEventSender(
            taskStreamKafkaTemplate,
            properties,
        )
}
