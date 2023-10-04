package ur.azizairo.foundation.views

import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import ur.azizairo.foundation.ARG_SCREEN
import ur.azizairo.foundation.SingletonScopeDependencies
import ur.azizairo.foundation.views.activity.ActivityDelegateHolder
import java.lang.reflect.Constructor

/**
 * Use this method for getting view-models from your fragments
 */
inline fun <reified VM: ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {

    val application = requireActivity().application
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    // Getting ActivityScopeViewModel instance
    val activityScopeViewModel = (requireActivity() as ActivityDelegateHolder).delegate.getActivityScopeViewModel()

    // forming the list of available dependencies:
    // - singleton scope dependencies (repositories) -> from App class
    // - activity VM scope dependencies -> from MainViewModel
    // - screen VM scope dependencies -> screen args
    val dependencies = listOf(screen) + activityScopeViewModel.sideEffectMediators + SingletonScopeDependencies.getSingletonScopeDependencies(application)

    // creating factory
    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
): AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {

        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!

        // - SavedStateHandle is also dependency form screen VM scope, but we can obtain it only here,
        //   that's why merging it with the list of other dependencies:
        val dependenciesWithSavedStateHandle = dependencies + handle

        // generating the list of arguments to be passed into the view-model's constructor
        val arguments = findDependencies(constructor, dependenciesWithSavedStateHandle)

        // creating view-model
        return constructor.newInstance(*arguments.toTypedArray()) as T
     }

    private fun findDependencies(
        constructor: Constructor<*>,
        dependencies: List<Any>
    ): List<Any> {

        val args = mutableListOf<Any>()
        // here we iterate though view-model's constructor arguments and for each
        // argument we search dependency that can be assigned to the arguments
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }

}
