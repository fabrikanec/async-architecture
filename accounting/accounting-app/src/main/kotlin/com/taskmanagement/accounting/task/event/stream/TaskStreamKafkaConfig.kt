package com.taskmanagement.accounting.task.event.stream

import com.taskmanagement.task.event.stream.taskStreamMessageConverter
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
class TaskStreamKafkaConfig {

    @Bean
    fun taskStreamEventListener(
        applicationEventPublisher: ApplicationEventPublisher,
    ): TaskStreamEventListener =
        TaskStreamEventListener(
            applicationEventPublisher,
        )

    @Bean(taskStreamKafkaListenerContainerFactoryName)
    fun taskStreamKafkaListenerContainerFactory(
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
            setMessageConverter(taskStreamMessageConverter)
        }
}

const val taskStreamKafkaListenerContainerFactoryName =
    "taskStreamKafkaListenerContainerFactory"
const val taskStreamKafkaSpel =
    "\${task-stream.topic:task-stream}"
