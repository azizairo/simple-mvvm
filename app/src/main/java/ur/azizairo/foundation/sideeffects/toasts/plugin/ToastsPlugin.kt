package ur.azizairo.foundation.sideeffects.toasts.plugin

import android.content.Context
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.sideeffects.SideEffectPlugin

/**
 * Plugin for displaying toast message from view-models.
 * Allows adding [Toasts] interface to the view-model constructor.
 */
class ToastsPlugin: SideEffectPlugin<ToastsSideEffectMediator, Nothing> {

    override val mediatorClass: Class<ToastsSideEffectMediator>
        get() = ToastsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {

        return ToastsSideEffectMediator(applicationContext)
    }
}
