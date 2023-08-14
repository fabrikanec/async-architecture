package com.taskmanagement.tasktracker.task.event.flow.kafka

import com.taskmanagement.task.event.flow.TaskFlowEvent
import com.taskmanagement.task.event.flow.taskFlowMessageConverter
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.util.UUID

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(
    TaskFlowEventProperties::class,
)
class KafkaTaskFlowEventProducerConfig(
    private val properties: TaskFlowEventProperties,
) {

    @Bean
    fun taskFlowEventTopic() =
        TopicBuilder.name(properties.topic).build()

    @Bean
    fun taskFlowKafkaTemplate(
        producerTaskFlowFactory: ProducerFactory<UUID, TaskFlowEvent>,
    ): KafkaTemplate<UUID, TaskFlowEvent> =
        KafkaTemplate(producerTaskFlowFactory).apply {
            messageConverter = taskFlowMessageConverter
        }

    @Bean
    fun kafkaTaskFlowEventSender(
        taskFlowKafkaTemplate: KafkaTemplate<UUID, TaskFlowEvent>,
    ): TaskFlowEventSender =
        TaskFlowEventSender(
            taskFlowKafkaTemplate,
            properties,
        )
}
