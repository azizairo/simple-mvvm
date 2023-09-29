package ur.azizairo.simplemvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ur.azizairo.foundation.model.Result
import ur.azizairo.foundation.views.BaseFragment
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.databinding.PartResultBinding

fun <T> BaseFragment.renderSimpleResult(
    root: ViewGroup,
    result: Result<T>,
    onSuccess: (T) -> Unit
) {

    val binding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progress_bar && it.id != R.id.error_container }
                .forEach { it.visibility = View.VISIBLE}
            onSuccess(successData)
        }
    )

}

/**
 * Collect items from the specified [Flow] only when fragment is at least in STARTED state.
 */
fun <T> BaseFragment.collectFlow(flow: Flow<T>, onCollect: (T) -> Unit) {

    viewLifecycleOwner.lifecycleScope.launch {
        // this coroutine is cancelled in onDestroyView
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            // this coroutine is launched every time when onStart is called;
            // collecting is cancelled in onStop
            flow.collect {
                onCollect(it)
            }
        }
    }
}

fun BaseFragment.onTryAgain(root: View, onTryAgain: () -> Unit) {

    root.findViewById<Button>(R.id.try_again_button).setOnClickListener {
        onTryAgain()
    }
}

