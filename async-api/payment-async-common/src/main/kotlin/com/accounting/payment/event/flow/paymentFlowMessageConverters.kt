package com.accounting.payment.event.flow

import com.accounting.payment.taskObjectMapper
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter
import org.springframework.kafka.support.converter.RecordMessageConverter
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper

val paymentFlowMessageConverter: RecordMessageConverter by lazy {
    ByteArrayJsonMessageConverter(taskObjectMapper).apply {
        typeMapper = DefaultJackson2JavaTypeMapper().apply {
            typePrecedence = Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID
            idClassMapping = PaymentFlowEventMeta.TypeId.typeByValue
            classIdFieldName = PaymentFlowEventMeta.TypeId.header
        }
    }
}
