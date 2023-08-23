package com.taskmanagement.accounting.payment.schedule.config

import com.taskmanagement.accounting.payment.schedule.config.properties.PaymentScheduleJobProperties
import com.taskmanagement.accounting.payment.schedule.job.SchedulePaymentJob
import com.taskmanagement.tasktracker.util.schedule.scheduledMethodRunnable
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger

@Configuration
@EnableConfigurationProperties(
    PaymentScheduleJobProperties::class,
)
class PaymentScheduleJobSchedulerConfig(
    private val paymentScheduleJobProperties: PaymentScheduleJobProperties,
    private val schedulePaymentJob: SchedulePaymentJob,
) {

    @Bean
    fun scheduleManagerJobScheduler() =
        ThreadPoolTaskScheduler().apply {
            poolSize = DEFAULT_POOL_SIZE
            setThreadNamePrefix(POOL_THREAD_PREFIX)
            initialize()
            schedule(
                scheduledMethodRunnable(
                    schedulePaymentJob,
                    SchedulePaymentJob::run,
                ),
                CronTrigger(paymentScheduleJobProperties.cron),
            )
        }

    companion object {
        private const val DEFAULT_POOL_SIZE = 10
        private const val POOL_THREAD_PREFIX = "payment-schedule-job-pool-"
    }
}
