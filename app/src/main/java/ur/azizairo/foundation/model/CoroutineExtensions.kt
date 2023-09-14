package ur.azizairo.foundation.model

import kotlinx.coroutines.CancellableContinuation
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun <T> CancellableContinuation<T>.toEmitter(): Emitter<T> {

    return object : Emitter<T> {

        var isDone = AtomicBoolean(false)

        override fun emit(finalResult: FinalResult<T>) {
            if (isDone.compareAndSet(false, true)) {
                when(finalResult) {
                    is ErrorResult -> resumeWithException(finalResult.exception)
                    is SuccessResult -> resume(finalResult.data)
                }
            }
        }

        override fun setCancelListener(cancelListener: CancelListener) {
            invokeOnCancellation { cancelListener.invoke() }
        }

    }
}