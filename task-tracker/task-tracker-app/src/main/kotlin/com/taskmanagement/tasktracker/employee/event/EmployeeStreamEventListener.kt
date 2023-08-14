package com.taskmanagement.tasktracker.employee.event

import com.taskmanagement.employee.event.stream.EmployeeStreamEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.transaction.annotation.Transactional

open class EmployeeStreamEventListener(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    @KafkaListener(
        topics = [employeeStreamKafkaSpel],
        containerFactory = employeeStreamKafkaListenerContainerFactoryName,
    )
    open fun consume(event: EmployeeStreamEvent) {
        applicationEventPublisher.publishEvent(event)
    }
}
