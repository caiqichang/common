package app.spring.common.util

import org.slf4j.LoggerFactory
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledFuture

@Component
class ScheduleManager {

    companion object {
        private val log = LoggerFactory.getLogger(ScheduleManager::class.java)
    }

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
            tasks.remove(taskId)
            return true
        }
        log.warn("[$taskId] task is not running")
        return false
    }

    fun startTask(taskId: String, cb: (ThreadPoolTaskScheduler) -> ScheduledFuture<*>): Boolean {
        if (!tasks.containsKey(taskId)) {
            tasks[taskId] = cb.invoke(scheduler)
            return true
        }
        log.warn("[$taskId] task is running")
        return false
    }
}