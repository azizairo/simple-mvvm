package ur.azizairo.foundation.model.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}
