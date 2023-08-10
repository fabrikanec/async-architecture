package com.taskmanagement.employee.event.stream

import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper

val employeeMessageConverter: RecordMessageConverter by lazy {
    ByteArrayJsonMessageConverter(employeeStreamObjectMapper).apply {
        typeMapper = DefaultJackson2JavaTypeMapper().apply {
            typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
            idClassMapping = EmployeeStreamEventMeta.TypeId.typeByValue
            classIdFieldName = EmployeeStreamEventMeta.TypeId.header
        }
    }
}
