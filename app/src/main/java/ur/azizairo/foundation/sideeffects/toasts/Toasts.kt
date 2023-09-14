package ur.azizairo.foundation.sideeffects.toasts

import ur.azizairo.foundation.sideeffects.toasts.plugin.ToastsPlugin

/**
 * Interface for showing toast messages to the user from view-models.
 * You need add [ToastsPlugin] to your activity before using this feature.
 */
interface Toasts {

    /**
     * Display a simple toast message
     */
    fun toast(message: String)
}
