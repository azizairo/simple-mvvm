package ur.azizairo.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ur.azizairo.simplemvvm.databinding.FragmentCurrentColorBinding
import ur.azizairo.simplemvvm.views.base.BaseFragment
import ur.azizairo.simplemvvm.views.base.BaseScreen
import ur.azizairo.simplemvvm.views.base.screenViewModel

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

        viewModel.currentColor.observe(viewLifecycleOwner) {
            binding.colorView.setBackgroundColor(it.value)
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        return binding.root
    }
}