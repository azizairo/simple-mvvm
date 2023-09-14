package ur.azizairo.foundation.views

import androidx.lifecycle.*
import kotlinx.coroutines.*
import ur.azizairo.foundation.model.ErrorResult
import ur.azizairo.foundation.model.Result
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.foundation.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

/**
 * Base class for all view-models.
 */
open class BaseViewModel: ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope: CoroutineScope = CoroutineScope(coroutineContext)

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }

    fun onBackPressed(): Boolean {

        clearViewModelScope()
        return false
    }

    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }

    /**
     * Launch task asynchronously and map its result to the specified
     * [liveResult].
     * Task is cancelled automatically if view-model is going to be destroyed.
     */
    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {

        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (exception: Exception) {
                liveResult.postValue(ErrorResult(exception))
            }
        }
    }

    private fun clearViewModelScope() {

        viewModelScope.cancel()
    }
}
