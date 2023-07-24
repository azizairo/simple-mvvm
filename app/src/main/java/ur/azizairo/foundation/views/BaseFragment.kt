package ur.azizairo.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import ur.azizairo.foundation.model.ErrorResult
import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.Result
import ur.azizairo.foundation.model.SuccessResult

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

    fun<T> renderResult(
        root: ViewGroup,
        result: Result<T>,
        onPending: () -> Unit,
        onError: () -> Unit,
        onSuccess: (T) -> Unit
    ) {
        root.children.forEach { it.visibility = View.GONE }
        when(result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError()
            is PendingResult -> onPending()
        }
    }

}
