package com.taskmanagement.tasktracker.task.price

import org.springframework.stereotype.Service

@Service
object PriceResolver { // it might be in adapter-service
    val priceToCharge
        get() = (-20..-10).random().toBigInteger()

    val priceToPay
        get() = (20..40).random().toBigInteger()
}
