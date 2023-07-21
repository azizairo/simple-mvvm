package ur.azizairo.foundation.navigator

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ur.azizairo.foundation.ARG_SCREEN
import ur.azizairo.foundation.utils.Event
import ur.azizairo.foundation.views.BaseFragment
import ur.azizairo.foundation.views.BaseScreen
import ur.azizairo.foundation.views.HasScreenTitle

class StackFragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val defaultTitle: String,
    private val animations: Animations,
    private val initialScreenCreator: () -> BaseScreen
): Navigator {

    private var result: Event<Any>? = null

     override fun launch(screen: BaseScreen) {

        launchFragment(screen)
    }

    override fun goBack(result: Any?) {

        if (result != null) {
            this.result = Event(result)
        }
        activity.onBackPressed()
    }

    fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState == null) {
            //define the initial screen that should be launched when app starts.
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    fun onDestroy() {

        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

     private fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {

         // as screen classes are inside fragments -> we can create fragment directly from screen
         val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
         fragment.arguments = bundleOf(ARG_SCREEN to screen)

         val transaction = activity.supportFragmentManager.beginTransaction()
         if (addToBackStack) transaction.addToBackStack(null)
         transaction
             .setCustomAnimations(
                 animations.enterAnim,
                 animations.exitAnim,
                 animations.popEnterAnim,
                 animations.popExitAnim
             )
             .replace(containerId, fragment)
             .commit()
     }

    fun notifyScreenUpdates() {

        val f = activity.supportFragmentManager.findFragmentById(containerId)

        if (activity.supportFragmentManager.backStackEntryCount > 0) {
            //more than 1 screen -> show back button in the toolbar
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if(f is HasScreenTitle && f.getScreenTitle() != null) {
            //fragment has custom screen title -> display it
            activity.supportActionBar?.title = f.getScreenTitle()
        } else {
            activity.supportActionBar?.title = defaultTitle
        }

    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {

            notifyScreenUpdates()
            publishResult(f)
        }
    }

    private fun publishResult(fragment: Fragment) {

        val result = result?.getValue() ?: return
        if (fragment is BaseFragment) {
            // has result that can be delivered to the screen's view-model
            fragment.viewModel.onResult(result)
        }
    }

    class Animations(
        @AnimRes val enterAnim: Int,
        @AnimRes val exitAnim: Int,
        @AnimRes val popEnterAnim: Int,
        @AnimRes val popExitAnim: Int
    )

}
