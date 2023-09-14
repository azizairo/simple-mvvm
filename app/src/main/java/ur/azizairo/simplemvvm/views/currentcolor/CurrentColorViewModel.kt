package ur.azizairo.simplemvvm.views.currentcolor

import android.Manifest
import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.foundation.model.takeSuccess
import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import ur.azizairo.foundation.model.tasks.factories.TasksFactory
import ur.azizairo.foundation.sideeffects.dialogs.Dialogs
import ur.azizairo.foundation.sideeffects.dialogs.plugin.DialogConfig
import ur.azizairo.foundation.sideeffects.intents.Intents
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.model.colors.ColorListener
import ur.azizairo.simplemvvm.model.colors.ColorsRepository
import ur.azizairo.simplemvvm.model.colors.NamedColor
import ur.azizairo.foundation.sideeffects.navigator.Navigator
import ur.azizairo.foundation.sideeffects.permissions.Permissions
import ur.azizairo.foundation.sideeffects.permissions.plugin.PermissionStatus
import ur.azizairo.foundation.sideeffects.resources.Resources
import ur.azizairo.foundation.sideeffects.toasts.Toasts
import ur.azizairo.foundation.uiactions.UiActions
import ur.azizairo.foundation.views.BaseViewModel
import ur.azizairo.foundation.views.LiveResult
import ur.azizairo.foundation.views.MutableLiveResult
import ur.azizairo.simplemvvm.views.changecolor.ChangeColorFragment

class CurrentColorViewModel(
        private val navigator: Navigator,
        private val toasts: Toasts,
        private val resources: Resources,
        private val permissions: Permissions,
        private val intents: Intents,
        private val dialogs: Dialogs,
        private val taskFactory: TasksFactory,
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
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    // ---

    fun changeColor() {

        val currentColor = currentColor.value.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun requestPermission() = taskFactory.async<Unit> {

        val permission = Manifest.permission.ACCESS_COARSE_LOCATION
        val hasPermission = permissions.hasPermission(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog()).await()
        } else {
            when(permissions.requestPermission(permission).await()) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_already_granted))
                }
                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }
                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAndAskForLaunchingAppSettingsDialog()).await()) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }.safeEnqueue()

    fun tryAgain() {

        load()
    }

    private fun load() {

        colorsRepository.getCurrentColor().into(_currentColor)
    }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
            title = resources.getString(R.string.dialog_permissions_title),
            message = resources.getString(R.string.permissions_already_granted),
            positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAndAskForLaunchingAppSettingsDialog() = DialogConfig(
            title = resources.getString(R.string.dialog_permissions_title),
            message = resources.getString(R.string.open_app_settings_message),
            positiveButton = resources.getString(R.string.action_open),
            negativeButton = resources.getString(R.string.action_cancel)
    )
}
