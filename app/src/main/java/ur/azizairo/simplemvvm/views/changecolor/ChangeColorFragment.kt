package ur.azizairo.simplemvvm.views.changecolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.GridLayoutManager
import ur.azizairo.simplemvvm.R
import ur.azizairo.simplemvvm.databinding.FragmentChangeColorBinding
import ur.azizairo.foundation.views.HasScreenTitle
import ur.azizairo.foundation.views.BaseFragment
import ur.azizairo.foundation.views.BaseScreen
import ur.azizairo.foundation.views.screenViewModel

/**
 * Screen for changing color.
 * 1) Displays the list of available colors
 * 2) Allows choosing the desired color
 * 3) Chosen color is saved only after pressing "Save" button
 * 4) The current choice is saved via [SavedStateHandle] (see [ChangeColorViewModel])
 */
class ChangeColorFragment: BaseFragment(), HasScreenTitle {

    override val viewModel by screenViewModel<ChangeColorViewModel>()

    /**
     * This screen has 1 argument: color ID to be displayed as selected.
     */
    class Screen(
        val currentColorId: Long
    ): BaseScreen

    /**
     * Example of dynamic screen title
     */
    override fun getScreenTitle(): String? {
        return viewModel.screenTitle.value
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentChangeColorBinding.inflate(inflater, container, false)

        val adapter = ColorsAdapter(viewModel)
        setupLayoutManager(binding, adapter)

        binding.saveButton.setOnClickListener { viewModel.onSavePressed() }
        binding.cancelButton.setOnClickListener { viewModel.onCancelPressed() }

        viewModel.colorList.observe(viewLifecycleOwner) {
            adapter.items = it
        }

        viewModel.screenTitle.observe(viewLifecycleOwner) {
            // if screen title is changed -> need to notify activity about updates
            notifyScreenUpdates()
        }

        return binding.root
    }

    private fun setupLayoutManager(binding: FragmentChangeColorBinding, adapter: ColorsAdapter) {

        //waiting for list width
        binding.colorsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener ( object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                binding.colorsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val width = binding.colorsRecyclerView.width
                val itemWidth = resources.getDimensionPixelSize(R.dimen.item_width)
                val columns = width / itemWidth
                binding.colorsRecyclerView.adapter = adapter
                binding.colorsRecyclerView.layoutManager = GridLayoutManager(
                    requireContext(), columns
                )
            }
        })
    }
}