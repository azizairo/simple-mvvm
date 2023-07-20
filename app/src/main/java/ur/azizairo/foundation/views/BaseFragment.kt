package ur.azizairo.foundation.views

import androidx.fragment.app.Fragment
import ur.azizairo.simplemvvm.MainActivity

/**
 * Base class for all fragments
 */
abstract class BaseFragment: Fragment() {

    /**
     * View-model that manages this fragment
     */
    abstract val viewModel: BaseViewModel

    /**
     * Call this method when activity controls (e.g. toolbar) should ve re-rendered
     */
    fun notifyScreenUpdates() {

        // if you have more than 1 activity -> you should use a separate interface instead of direct
        // cast to MainActivity
        ((requireActivity()) as FragmentsHolder).notifyScreenUpdates()
    }

}
