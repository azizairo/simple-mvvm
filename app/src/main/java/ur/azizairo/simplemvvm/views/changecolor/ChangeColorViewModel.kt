package ur.azizairo.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.model.colors.ColorsRepository
import ur.azizairo.simplemvvm.model.colors.NamedColor
import ur.azizairo.foundation.sideeffects.navigator.Navigator
import ur.azizairo.foundation.sideeffects.resources.Resources
import ur.azizairo.foundation.sideeffects.toasts.Toasts
import ur.azizairo.foundation.views.BaseViewModel
import ur.azizairo.foundation.views.LiveResult
import ur.azizairo.foundation.views.MediatorLiveResult
import ur.azizairo.foundation.views.MutableLiveResult
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
    private val _availableColors = MutableLiveResult<List<NamedColor>>(PendingResult())
    private val _currentColorId = savedStateHandle.getLiveData(
        "currentColorId", screen.currentColorId
    )
    private val _saveInProgress = MutableLiveData(false)

    // main destination (contains merged values from _availableColors & _currentColorId)
    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    // side destination, also the same result can be achieved by using Transformations.map() function
    val screenTitle: LiveData<String> = Transformations.map(viewState) { result ->
        if (result is SuccessResult) {
            val currentColor = result.data.colorsList.first { it.selected }
            resources.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
        } else {
            resources.getString(R.string.change_color_screen_title_simple)
        }
    }

    init {

        load()

        //initializing MediatorLiveData
        _viewState.addSource(_availableColors) { mergeSources() }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }


    override fun onColorChosen(namedColor: NamedColor) {

        if (_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {

        try {
            _saveInProgress.postValue(true)

            val currentColorId =
                _currentColorId.value ?: throw IllegalArgumentException("Color ID should not be null")
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor)

            navigator.goBack(currentColor)
        } catch (exception: Exception) {
            if(exception !is CancellationException) {
                toasts.toast(resources.getString(R.string.error_happened))
            }
        } finally {
            _saveInProgress.value = false
        }
    }

    fun onCancelPressed() {

        navigator.goBack()
    }

    fun tryAgain() {

        load()
    }

    /**
     * [MediatorLiveData] can listen other LiveData instances (even more than 1)
     * and combine their values.
     * Here we listen the list of available colors ([_availableColors] live-data) + current color id
     * ([_currentColorId] live-data), then we use both of these values in order to create a list of
     * [NamedColorListItem], it is a list to be displayed in RecyclerView.
     */
    private fun mergeSources() {

        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return

        _viewState.value = colors.map { colorsList ->
            ViewState(
                colorsList = colorsList.map {
                    NamedColorListItem(it, currentColorId == it.id)
                },
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress
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
        val showSaveProgressBar: Boolean
    )

}
