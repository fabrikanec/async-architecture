package com.taskmanagement.task.event.stream

import com.taskmanagement.task.taskObjectMapper
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper

val taskStreamMessageConverter: RecordMessageConverter by lazy {
    ByteArrayJsonMessageConverter(taskObjectMapper).apply {
        typeMapper = DefaultJackson2JavaTypeMapper().apply {
            typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
            idClassMapping = TaskStreamEventMeta.TypeId.typeByValue
            classIdFieldName = TaskStreamEventMeta.TypeId.header
        }
    }
}
