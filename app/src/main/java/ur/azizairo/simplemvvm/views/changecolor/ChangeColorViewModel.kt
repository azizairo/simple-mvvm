package ur.azizairo.simplemvvm.views.changecolor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.model.colors.ColorsRepository
import ur.azizairo.simplemvvm.model.colors.NamedColor
import ur.azizairo.foundation.navigator.Navigator
import ur.azizairo.foundation.uiactions.UiActions
import ur.azizairo.foundation.views.BaseViewModel
import ur.azizairo.simplemvvm.views.changecolor.ChangeColorFragment.Screen

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle
): BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors = MutableLiveData<List<NamedColor>>()
    private val _currentColorId = savedStateHandle.getLiveData(
        "currentColorId", screen.currentColorId
    )

    // main destination (contains merged values from _availableColors & _currentColorId)
    private val _colorList = MediatorLiveData<List<NamedColorListItem>>()
    val colorList: LiveData<List<NamedColorListItem>> = _colorList

    // side destination, also the same result can be achieved by using Transformations.map() function
    private val _screenTitle = MutableLiveData<String>()
    val screenTitle: LiveData<String> = _screenTitle

    init {
        _availableColors.value = colorsRepository.getAvailableColors()
        //initializing MediatorLiveData
        _colorList.addSource(_availableColors) { mergeSources() }
        _colorList.addSource(_currentColorId) { mergeSources() }
    }


    override fun onColorChosen(namedColor: NamedColor) {

        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() {

        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorsRepository.getById(currentColorId)
        colorsRepository.currentColor = currentColor
        navigator.goBack(result = currentColor)
    }

    fun onCancelPressed() {

        navigator.goBack()
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
        val currentColor = colors.first { it.id == currentColorId }
        _colorList.value = colors.map { NamedColorListItem(it, currentColorId == it.id) }
        _screenTitle.value  = uiActions.getString(
            R.string.change_color_screen_title, currentColor.name
        )
    }

}