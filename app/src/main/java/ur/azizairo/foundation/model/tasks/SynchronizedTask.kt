package ur.azizairo.foundation.model.tasks

import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import java.util.concurrent.atomic.AtomicBoolean

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
