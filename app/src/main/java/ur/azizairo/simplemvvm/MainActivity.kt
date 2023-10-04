package ur.azizairo.simplemvvm

import android.os.Bundle
import ur.azizairo.foundation.sideeffects.SideEffectPluginsManager
import ur.azizairo.foundation.sideeffects.dialogs.plugin.DialogsPlugin
import ur.azizairo.foundation.sideeffects.intents.plugin.IntentsPlugin
import ur.azizairo.foundation.sideeffects.navigator.plugin.NavigatorPlugin
import ur.azizairo.foundation.sideeffects.navigator.plugin.StackFragmentNavigator
import ur.azizairo.foundation.sideeffects.permissions.plugin.PermissionsPlugin
import ur.azizairo.foundation.sideeffects.resources.plugin.ResourcesPlugin
import ur.azizairo.foundation.sideeffects.toasts.plugin.ToastsPlugin
import ur.azizairo.foundation.views.activity.BaseActivity
import ur.azizairo.simplemvvm.views.currentcolor.CurrentColorFragment

/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens.
 */
class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with(manager) {

        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Initializer.init()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }

    private fun createNavigator() = StackFragmentNavigator(
            containerId = R.id.fragment_container,
            defaultTitle = getString(R.string.app_name),
            animations = StackFragmentNavigator.Animations(
                    enterAnim = R.anim.enter,
                    exitAnim = R.anim.exit,
                    popEnterAnim = R.anim.pop_enter,
                    popExitAnim = R.anim.pop_exit
            ),
            initialScreenCreator = { CurrentColorFragment.Screen() }
    )
}
