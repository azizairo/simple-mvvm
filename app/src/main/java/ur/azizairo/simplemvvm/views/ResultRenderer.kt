package ur.azizairo.simplemvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
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

fun BaseFragment.onTryAgain(root: View, onTryAgain: () -> Unit) {

    root.findViewById<Button>(R.id.try_again_button).setOnClickListener {
        onTryAgain()
    }
}

