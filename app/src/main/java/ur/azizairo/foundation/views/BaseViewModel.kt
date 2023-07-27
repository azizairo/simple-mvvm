package ur.azizairo.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.Result
import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.model.tasks.TaskListener
import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import ur.azizairo.foundation.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class for all view-models.
 */
open class BaseViewModel(
    private val dispatcher: Dispatcher
): ViewModel() {

    private val tasks = mutableSetOf<Task<*>>()

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }

    fun onBackPressed() {
        clearTasks()
    }

    override fun onCleared() {
        super.onCleared()
        clearTasks()
    }

    fun <T> Task<T>.safeEnqueue(listener: TaskListener<T>? = null) {

        tasks.add(this)
        this.enqueue(dispatcher) {
            tasks.remove(this)
            listener?.invoke(it)
        }
    }

    fun <T> Task<T>.into(liveResult: MutableLiveResult<T>) {

        liveResult.value = PendingResult()
        this.safeEnqueue {
            liveResult.value = it
        }
    }

    private fun clearTasks() {
        tasks.forEach {it.cancel()}
        tasks.clear()
    }
}
