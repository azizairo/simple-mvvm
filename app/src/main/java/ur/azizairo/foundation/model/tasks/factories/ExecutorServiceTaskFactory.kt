package ur.azizairo.foundation.model.tasks.factories

import ur.azizairo.foundation.model.tasks.AbstractTask
import ur.azizairo.foundation.model.tasks.SynchronizedTask
import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.model.tasks.TaskListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

/**
 * Factory that crates tasks which are launched by the specified [ExecutorService].
 * For example you may pass [Executors.newCachedThreadPool] in order to use a pool of cached threads
 * or [Executors.newSingleThreadExecutor] for launching task one by one.
 */
class ExecutorServiceTaskFactory(
    private val executorService: ExecutorService
): TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {

        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class ExecutorServiceTask<T>(
        private val body: TaskBody<T>
    ): AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {

            future = executorService.submit {
                executeBody(body, listener)
            }
        }

        override fun doCancel() {

            future?.cancel(true)
        }
    }
}