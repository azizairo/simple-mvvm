package ur.azizairo.foundation.model.tasks.callback

import ur.azizairo.foundation.model.FinalResult
import ur.azizairo.foundation.model.tasks.AbstractTask
import ur.azizairo.foundation.model.tasks.SynchronizedTask
import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.model.tasks.TaskListener

/**
 * Converts callbacks into [Task].
 * For example if some library or Android SDK provides callback for some operations
 * (e.g. dialog button listeners, location request, etc.) then this task can be used for wrapping
 * callbacks into [Task] interface.
 *
 * Usage example
 * ```
 * val task = CallbackTask.create { emitter ->
 *   val someNetworkCall: NetworkCall<User> = getUser("username")
 *
 *   emitter.setCancelListener { someNetworkCall.cancel() }
 *
 *   someNetworkCall.fetch(object : Callback<User> {
 *     override fun onSuccess(user: User) {
 *       emitter.emit(SuccessResult(user))
 *     }
 *
 *     override fun onError(error: Exception) {
 *       emitter.emit(ErrorResult(error))
 *     }
 *   })
 * }
 * ```
 *
 */
class CallbackTask<T> private constructor(
        private val executionListener: ExecutionListener<T>
): AbstractTask<T>() {
 
    private var emitter: EmitterImpl<T>? = null

    override fun doEnqueue(listener: TaskListener<T>) {

        emitter = EmitterImpl(listener).also{ executionListener.invoke(it) }
    }

    override fun doCancel() {

        emitter?.onCancelListener?.invoke()
    }

    companion object {

        fun <T> create(executionListener: ExecutionListener<T>): Task<T> {

            return SynchronizedTask(CallbackTask(executionListener))
        }
    }

    private class EmitterImpl<T>(
            private val taskListener: TaskListener<T>
    ): Emitter<T> {

        var onCancelListener: CancelListener? = null

        override fun emit(finalResult: FinalResult<T>) {

            taskListener.invoke(finalResult)
        }

        override fun setCancelListener(cancelListener: CancelListener) {

            this.onCancelListener = cancelListener
        }

    }
}
