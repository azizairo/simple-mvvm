package ur.azizairo.simplemvvm

import android.app.Application
import ur.azizairo.foundation.BaseApplication
import ur.azizairo.foundation.model.tasks.SimpleTasksFactory
import ur.azizairo.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App: Application(), BaseApplication {

    private val tasksFactory = SimpleTasksFactory()

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories = listOf(
        tasksFactory,
        InMemoryColorsRepository(tasksFactory)
    )

}
