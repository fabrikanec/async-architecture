package com.taskmanagement.accounting.task.event.flow

import com.taskmanagement.task.event.flow.taskFlowMessageConverter
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@Configuration(proxyBeanMethods = false)
class TaskFlowKafkaConfig {

    @Bean
    fun taskFlowEventListener(
        applicationEventPublisher: ApplicationEventPublisher,
    ): TaskFlowEventListener =
        TaskFlowEventListener(
            applicationEventPublisher,
        )

    @Bean(taskFlowKafkaListenerContainerFactoryName)
    fun taskFlowKafkaListenerContainerFactory(
        kafkaProperties: KafkaProperties,
        configurer: ConcurrentKafkaListenerContainerFactoryConfigurer,
        kafkaConsumerFactory: ObjectProvider<ConsumerFactory<Any?, Any?>>,
    ): ConcurrentKafkaListenerContainerFactory<*, *> =
        ConcurrentKafkaListenerContainerFactory<Any?, Any?>().apply {
            configurer.configure(
                this,
                kafkaConsumerFactory.getIfAvailable {
                    DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties())
                }
            )
            setMessageConverter(taskFlowMessageConverter)
        }
}

const val taskFlowKafkaListenerContainerFactoryName =
    "taskFlowKafkaListenerContainerFactory"
const val taskFlowKafkaSpel =
    "\${task-flow.topic:task-flow}"
