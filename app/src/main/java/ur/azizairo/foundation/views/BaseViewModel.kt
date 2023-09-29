package ur.azizairo.foundation.views

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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

    /**
     * Launch the specified suspending [block] and use its result as a value for the
     * provided [stateFlow].
     */
    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block: suspend () -> T) {

        viewModelScope.launch {
            try {
                stateFlow.value = SuccessResult(block())
            } catch (exception: Exception) {
                stateFlow.value = ErrorResult(exception)
            }
        }
    }

    /**
     * Create a [MutableStateFlow] which reflects a state of value with the
     * specified key managed by [SavedStateHandle]. When the value is updated,
     * the instance of [MutableStateFlow] emits a new item with the updated value.
     * When some new value is assigned to the [MutableStateFlow] via [MutableStateFlow.value]
     * it is recorded into [SavedStateHandle]. So actually this method creates a
     * [MutableStateFlow] which works in the same way as [MutableLiveData] returned
     * by [SavedStateHandle.getLiveData].
     */
    fun <T> SavedStateHandle.getStateFlow(key: String, initialValue: T): MutableStateFlow<T> {

        val savedStateHandel = this
        val mutableStateFlow = MutableStateFlow(savedStateHandel[key] ?: initialValue)

        viewModelScope.launch {
            mutableStateFlow.collect{
                savedStateHandel[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandel.getLiveData<T>(key).asFlow().collect {
                mutableStateFlow.value = it
            }
        }

        return mutableStateFlow
    }

    private fun clearViewModelScope() {

        viewModelScope.cancel()
    }
}
