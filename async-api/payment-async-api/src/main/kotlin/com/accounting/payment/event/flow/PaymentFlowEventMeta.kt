package com.accounting.payment.event.flow

object PaymentFlowEventMeta {
    object TypeId {
        private const val PAYMENT_SCHEDULED_V1 = "PAYMENT_SCHEDULED_V1"

        const val header: String = "__TypeId__"

        val typeByValue: Map<String, Class<*>> = mapOf(
            PAYMENT_SCHEDULED_V1 to PaymentScheduledEventV1::class.java,
        )
    }
}
