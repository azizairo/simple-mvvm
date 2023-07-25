package ur.azizairo.foundation.model.tasks

import android.os.Handler
import android.os.Looper

private val handler = Handler(Looper.getMainLooper())

// TODO!!
class SimpleTasksFactory: TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        TODO("Not yet implemented")
    }

    // TODO!!!
    class SimpleTask<T>(
        private val body: TaskBody<T>
    ): Task<T> {

        var thread: Thread? = null

        override fun await(): T = body()

        override fun enqueue(listener: TaskListener<T>) {

            thread = Thread {
                try {
                    val data = body()
                } catch (e: Exception) {

                }
            }
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

    }
}
