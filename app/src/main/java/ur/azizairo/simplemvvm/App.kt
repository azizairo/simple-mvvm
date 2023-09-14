package ur.azizairo.simplemvvm

import android.app.Application
import kotlinx.coroutines.Dispatchers
import ur.azizairo.foundation.BaseApplication
import ur.azizairo.foundation.model.coroutines.IoDispatcher
import ur.azizairo.foundation.model.coroutines.WorkerDispatcher
import ur.azizairo.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App: Application(), BaseApplication {

    private val ioDispatcher = IoDispatcher(Dispatchers.IO)
    private val workerDispatcher = WorkerDispatcher(Dispatchers.Default)

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val singletonScopeDependencies = listOf(
        InMemoryColorsRepository(ioDispatcher)
    )

}
