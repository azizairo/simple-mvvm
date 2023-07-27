package ur.azizairo.foundation.model.tasks

import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Wrapper class for other task.
 * Contains common synchronization logic. Ensures that method of the wrapped task
 * are executed in correct order. Doesn't allow cases: e.g. launching task more than 1 time,
 * triggered listeners more than 1 time, launching task after cancelling, cancelling already
 * finished task and so on.
 */
class SynchronizedTask<T>(
    private val task: Task<T>
): Task<T> {

    @Volatile
    private var canceled = false

    private var executed = false

    private var listenerCalled = AtomicBoolean(false)

    override fun await(): T {

        synchronized(this) {
            if (canceled) {
                throw CancelledException()
            }
            if (executed) {
                throw IllegalStateException("Task has been executed.")
            }
            executed = true
        }
        return task.await()
    }

    override fun enqueue(
        dispatcher: Dispatcher, listener: TaskListener<T>
    ) = synchronized(this) {

        if (canceled) {
            return
        }
        if (executed) {
            throw IllegalStateException("Task has been executed.")
        }
        executed = true

        val finalListener: TaskListener<T> = { result ->
            if (listenerCalled.compareAndSet(false, true)) {
                if (!canceled) {
                    listener(result)
                }
            }
        }

        task.enqueue(dispatcher, finalListener)
    }

    override fun cancel() = synchronized(this) {

        if (listenerCalled.compareAndSet(false, true)) {
            if(canceled) {
                return
            }
            canceled = true
            task.cancel()
        }
    }

}
