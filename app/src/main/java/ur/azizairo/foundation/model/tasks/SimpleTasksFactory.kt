package ur.azizairo.foundation.model.tasks

import android.os.Handler
import android.os.Looper
import ur.azizairo.foundation.model.ErrorResult
import ur.azizairo.foundation.model.FinalResult
import ur.azizairo.foundation.model.SuccessResult

private val handler = Handler(Looper.getMainLooper())

// TODO!!!
class SimpleTasksFactory: TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {

        return SimpleTask(body)
    }

    // TODO!!!
    class SimpleTask<T>(
        private val body: TaskBody<T>
    ): Task<T> {

        var thread: Thread? = null
        var canceled = false

        override fun await(): T = body()

        override fun enqueue(listener: TaskListener<T>) {

            thread = Thread {
                try {
                    val data = body()
                    publishResult(listener, SuccessResult(data))
                } catch (e: Exception) {
                    publishResult(listener, ErrorResult(e))
                }
            }. apply { start() }
        }

        override fun cancel() {

            canceled = true
            thread?.interrupt()
            thread = null
        }

        private fun publishResult(listener: TaskListener<T>, result: FinalResult<T>) {

            handler.post {
                if (canceled) return@post
                listener(result)
            }
        }

    }
}
