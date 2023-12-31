package ur.azizairo.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ur.azizairo.foundation.model.*
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.model.colors.ColorsRepository
import ur.azizairo.simplemvvm.model.colors.NamedColor
import ur.azizairo.foundation.sideeffects.navigator.Navigator
import ur.azizairo.foundation.sideeffects.resources.Resources
import ur.azizairo.foundation.sideeffects.toasts.Toasts
import ur.azizairo.foundation.utils.finiteShareIn
import ur.azizairo.foundation.views.BaseViewModel
import ur.azizairo.simplemvvm.views.changecolor.ChangeColorFragment.Screen
import kotlin.Exception

class ChangeColorViewModel(
        screen: Screen,
        private val navigator: Navigator,
        private val toasts: Toasts,
        private val resources: Resources,
        private val colorsRepository: ColorsRepository,
        savedStateHandle: SavedStateHandle,
): BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableStateFlow<Result<List<NamedColor>>>(PendingResult())
    private val _currentColorId = savedStateHandle.getStateFlow(
        "currentColorId", screen.currentColorId
    )
    private val _instantSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampleSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)

    // main destination (contains merged values from _availableColors & _currentColorId)
    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _instantSaveInProgress,
        _sampleSaveInProgress,
        ::mergeSources
    )

    // side destination, also the same result can be achieved by using Transformations.map() function
    // example of converting Flow into LiveData
    // - incoming flow is Flow<Result<ViewState>>
    // - Flow<Result<ViewState>> is mapped to Flow<String> by using .map() operator
    // - then Flow<String> is converted to LiveData<String> by using .asLiveData()
    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resources.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
            } else {
                resources.getString(R.string.change_color_screen_title_simple)
            }
        }
        .asLiveData()

    init {

        load()
    }


    override fun onColorChosen(namedColor: NamedColor) {

        if (_instantSaveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {

        try {
            _instantSaveInProgress.value = PercentageProgress.START
            _sampleSaveInProgress.value = PercentageProgress.START

            val currentColorId =
                _currentColorId.value ?: throw IllegalArgumentException("Color ID should not be null")
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor)
                .finiteShareIn(this)

            val instantJob = async {
                flow.collect { percentage ->
                    _instantSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            val sampledJob = async {
                flow.sample(200).collect { percentage ->
                    _sampleSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            instantJob.await()
            sampledJob.await()

            navigator.goBack(currentColor)
        } catch (exception: Exception) {
            if(exception !is CancellationException) {
                toasts.toast(resources.getString(R.string.error_happened))
            }
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampleSaveInProgress.value = EmptyProgress
        }
    }

    fun onCancelPressed() {

        navigator.goBack()
    }

    fun tryAgain() {

        load()
    }

    /**
     * Transformation pure method for combining data from several input flows:
     * - the result of fetching colors list (Result<List<NamedColor>>)
     * - current selected color in RecyclerView (Long)
     * - flag whether saving operation is in progress or not (Boolean)
     * All values above are merged into one [ViewState] instance:
     * ```
     * Flow<Result<List<NamedColor>>> ---+
     * Flow<Long> -----------------------|--> Flow<Result<ViewState>>
     * Flow<Boolean> --------------------+
     * ```
     */
    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        instantSaveInProgress: Progress,
        sampleSaveInProgress: Progress
        ): Result<ViewState> {

        return colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map {
                    NamedColorListItem(it, currentColorId == it.id)
                },
                showSaveButton = !instantSaveInProgress.isInProgress(),
                showCancelButton = !instantSaveInProgress.isInProgress(),
                showSaveProgressBar = instantSaveInProgress.isInProgress(),
                saveProgressPercentage = instantSaveInProgress.getPercentage(),
                saveProgressPercentageMessage = resources.getString(
                        R.string.percentage_value, sampleSaveInProgress.getPercentage()
                )
            )
        }
    }

    private fun load() = into(_availableColors) {

        colorsRepository.getAvailableColors()
    }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,
        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String
    )

}
