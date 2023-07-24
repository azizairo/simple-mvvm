package ur.azizairo.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ur.azizairo.foundation.model.ErrorResult
import ur.azizairo.foundation.model.PendingResult
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.simplemvvm.databinding.FragmentCurrentColorBinding
import ur.azizairo.foundation.views.BaseFragment
import ur.azizairo.foundation.views.BaseScreen
import ur.azizairo.foundation.views.screenViewModel
import ur.azizairo.simplemvvm.databinding.PartResultBinding
import ur.azizairo.simplemvvm.views.onTryAgain
import ur.azizairo.simplemvvm.views.renderSimpleResult

class CurrentColorFragment: BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = {
                    binding.colorView.setBackgroundColor(it.value)
                }
            )
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        return binding.root
    }
}