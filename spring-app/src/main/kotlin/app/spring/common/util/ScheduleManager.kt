package app.spring.common.util

import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.ScheduledFuture

object ScheduleManager {

    private val log = LoggerFactory.getLogger(ScheduleManager::class.java)

    private val tasks = mutableMapOf<String, ScheduledFuture<*>>()
    private val scheduler = ThreadPoolTaskScheduler()

    init {
        scheduler.initialize()
    }

    fun isTaskRunning(taskId: String): Boolean {
        return tasks.containsKey(taskId)
    }

    fun stopTask(taskId: String): Boolean {
        val task = tasks[taskId]
        if (task != null) {
            task.cancel(true)
            tasks -= taskId
            return true
        }
        log.warn("[$taskId] task is not running")
        return false
    }

    fun startTask(taskId: String, cb: (ThreadPoolTaskScheduler) -> ScheduledFuture<*>): Boolean {
        if (taskId !in tasks.keys) {
            tasks[taskId] = cb.invoke(scheduler)
            return true
        }
        log.warn("[$taskId] task is running")
        return false
    }
}