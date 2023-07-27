package ur.azizairo.foundation.model.tasks.factories

import ur.azizairo.foundation.model.tasks.Task

typealias TaskBody<T> = () -> T

/**
 * Factory for async task instance ([Task]) from synchronous code defined by [TaskBody]
 */
interface TasksFactory {

    /**
     * Create a new [Task] instance from the specified body.
     */
    fun <T> async(body: TaskBody<T>): Task<T>

}
