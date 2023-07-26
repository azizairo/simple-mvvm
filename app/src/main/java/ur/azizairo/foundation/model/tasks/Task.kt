package ur.azizairo.foundation.model.tasks

import ur.azizairo.foundation.model.FinalResult
import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(originalException: Exception? = null): Exception(originalException)

/**
 * Base interface for all async operations.
 */
interface Task<T> {

    /**
     * Blocking method for waiting and getting results.
     * Throws exception in case of error.
     * @throws [IllegalStateException] if task has been already executed
     */
    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task is cancelled before finishing, listener is not called.
     *
     * Listeners are called in main thread
     * @throws [IllegalStateException] if task has been already executed.
     */
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue]
     */
    fun cancel()

}
