package com.taskmanagement.task.event.flow

import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper

val taskMessageConverter: RecordMessageConverter by lazy {
    ByteArrayJsonMessageConverter(taskObjectMapper).apply {
        typeMapper = DefaultJackson2JavaTypeMapper().apply {
            typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
            idClassMapping = TaskFlowEventMeta.TypeId.typeByValue
            classIdFieldName = TaskFlowEventMeta.TypeId.header
        }
    }
}
