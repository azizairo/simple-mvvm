package ur.azizairo.simplemvvm

import android.app.Application
import ur.azizairo.foundation.BaseApplication
import ur.azizairo.foundation.model.tasks.SimpleTasksFactory
import ur.azizairo.foundation.model.tasks.ThreadUtils
import ur.azizairo.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ur.azizairo.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App: Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()
    private val threadUtils = ThreadUtils.DefaultThreadUtils()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val singletonScopeDependencies = listOf(
        tasksFactory,
        dispatcher,
        InMemoryColorsRepository(tasksFactory, threadUtils)
    )

}
