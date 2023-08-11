package com.taskmanagement.tasktracker.config

import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager

@Configuration(proxyBeanMethods = false)
class TransactionalConfig {
    @Bean
    @Primary
    fun transactionManager(
        transactionManagerCustomizers: ObjectProvider<TransactionManagerCustomizers>,
    ): PlatformTransactionManager =
        JpaTransactionManager().apply {
            transactionManagerCustomizers.ifAvailable { customizers: TransactionManagerCustomizers ->
                customizers.customize(this)
            }
        }
}
