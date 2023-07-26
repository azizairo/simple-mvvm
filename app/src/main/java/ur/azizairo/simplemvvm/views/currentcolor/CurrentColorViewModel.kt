package ur.azizairo.simplemvvm.views.currentcolor

import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.foundation.model.takeSuccess
import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.model.colors.ColorListener
import ur.azizairo.simplemvvm.model.colors.ColorsRepository
import ur.azizairo.simplemvvm.model.colors.NamedColor
import ur.azizairo.foundation.navigator.Navigator
import ur.azizairo.foundation.uiactions.UiActions
import ur.azizairo.foundation.views.BaseViewModel
import ur.azizairo.foundation.views.LiveResult
import ur.azizairo.foundation.views.MutableLiveResult
import ur.azizairo.simplemvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
    dispatcher: Dispatcher
): BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(SuccessResult(it))
    }

    // --- example of listening results via model layer

    init {
        colorsRepository.addListener(colorListener)
        load()
    }

    override fun onCleared() {

        super.onCleared()
        colorsRepository.removeListener(colorListener)
    }

    // --- example of listening results directly from the screen
    override fun onResult(result: Any) {

        super.onResult(result)
        if (result is NamedColor) {
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
        }
    }

    // ---

    fun changeColor() {

        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() {

        load()
    }

    private fun load() {

        colorsRepository.getCurrentColor().into(_currentColor)
    }
}
