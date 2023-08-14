package com.taskmanagement.tasktracker.task.shuffle.config

import com.taskmanagement.tasktracker.task.shuffle.config.properties.TaskShuffleJobProperties
import com.taskmanagement.tasktracker.util.schedule.scheduledMethodRunnable
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.support.CronTrigger

@Configuration
@EnableConfigurationProperties(
    TaskShuffleJobProperties::class,
)
class TaskShuffleJobSchedulerConfig(
    private val taskShuffleJobProperties: TaskShuffleJobProperties,
    private val taskShuffleJob: ShuffleJob,
) {

    @Bean
    fun scheduleManagerJobScheduler() =
        ThreadPoolTaskScheduler().apply {
            poolSize = DEFAULT_POOL_SIZE
            setThreadNamePrefix(POOL_THREAD_PREFIX)
            initialize()
            schedule(
                scheduledMethodRunnable(
                    taskShuffleJob,
                    ShuffleJob::run,
                ),
                CronTrigger(taskShuffleJobProperties.cron),
            )
        }

    companion object {
        private const val DEFAULT_POOL_SIZE = 10
        private const val POOL_THREAD_PREFIX = "task-shuffle-job-pool-"
    }
}
