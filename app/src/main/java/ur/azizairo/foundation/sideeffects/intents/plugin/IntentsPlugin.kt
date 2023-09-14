package ur.azizairo.foundation.sideeffects.intents.plugin

import android.content.Context
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.sideeffects.SideEffectPlugin

/**
 * Plugin for launching system activities from view-models.
 * Allows adding [Intents] interface to the view-model constructor.
 */
class IntentsPlugin: SideEffectPlugin<IntentsSideEffectMediator, Nothing> {

    override val mediatorClass: Class<IntentsSideEffectMediator>
        get() = IntentsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {

        return IntentsSideEffectMediator(applicationContext)
    }
}
