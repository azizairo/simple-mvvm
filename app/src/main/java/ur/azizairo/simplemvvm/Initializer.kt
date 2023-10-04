package ur.azizairo.simplemvvm

import ur.azizairo.foundation.SingletonScopeDependencies
import ur.azizairo.foundation.model.coroutines.IoDispatcher
import ur.azizairo.foundation.model.coroutines.WorkerDispatcher
import ur.azizairo.simplemvvm.model.colors.InMemoryColorsRepository

object Initializer {

    fun init()  = SingletonScopeDependencies.init { applicationContext ->
        val ioDispatcher = IoDispatcher()
        val workerDispatcher = WorkerDispatcher()

        listOf(
            ioDispatcher,
            workerDispatcher,
            InMemoryColorsRepository(ioDispatcher)
        )
    }
}
