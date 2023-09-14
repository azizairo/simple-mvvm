package ur.azizairo.foundation.sideeffects.intents

import ur.azizairo.foundation.sideeffects.intents.plugin.IntentsPlugin

/**
 * Side-effects interface for launching some system activities.
 * You need to add [IntentsPlugin] to your activity before using this feature.
 */
interface Intents {

    /**
     * Open system settings for this application
     */
    fun openAppSettings()


}
