package com.taskmanagement.accounting.employee.event

import com.taskmanagement.employee.event.stream.employeeMessageConverter
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
class EmployeeStreamKafkaConfig {

    @Bean
    fun employeeStreamEventListener(
        applicationEventPublisher: ApplicationEventPublisher,
    ): EmployeeStreamEventListener =
        EmployeeStreamEventListener(
            applicationEventPublisher,
        )

    @Bean(employeeStreamKafkaListenerContainerFactoryName)
    fun employeeStreamKafkaListenerContainerFactory(
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
            setMessageConverter(employeeMessageConverter)
        }
}

const val employeeStreamKafkaListenerContainerFactoryName =
    "employeeStreamKafkaListenerContainerFactory"
const val employeeStreamKafkaSpel =
    "\${employee-stream.topic:employee-stream}"
