package ur.azizairo.foundation.sideeffects.dialogs

import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.sideeffects.dialogs.plugin.DialogConfig
import ur.azizairo.foundation.sideeffects.dialogs.plugin.DialogsPlugin

/**
 * Side-effects interface for managing dialogs from view-model.
 * You need to add [DialogsPlugin] to your activity before using this feature.
 *
 * WARN! Please note, dialogs don't survive after app killing.
 */
interface Dialogs {

    /**
     * Show alert dialog to the user and wait for the user choice.
     */
    fun show(dialogConfig: DialogConfig): Task<Boolean>
}
