package com.taskmanagement.accounting.payment.execution.config

import com.taskmanagement.accounting.payment.execution.config.properties.PaymentExecutionJobProperties
import com.taskmanagement.accounting.payment.execution.job.PaymentExecutionJob
import com.taskmanagement.tasktracker.util.schedule.scheduledMethodRunnable
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger

@Configuration
@EnableConfigurationProperties(
    PaymentExecutionJobProperties::class,
)
class PaymentExecutionJobSchedulerConfig(
    private val paymentExecutionJobProperties: PaymentExecutionJobProperties,
    private val paymentExecutionJob: PaymentExecutionJob,
) {

    @Bean
    fun scheduleManagerJobScheduler() =
        ThreadPoolTaskScheduler().apply {
            poolSize = DEFAULT_POOL_SIZE
            setThreadNamePrefix(POOL_THREAD_PREFIX)
            initialize()
            schedule(
                scheduledMethodRunnable(
                    paymentExecutionJob,
                    PaymentExecutionJob::run,
                ),
                CronTrigger(paymentExecutionJobProperties.cron),
            )
        }

    companion object {
        private const val DEFAULT_POOL_SIZE = 10
        private const val POOL_THREAD_PREFIX = "payment-execution-job-pool-"
    }
}
