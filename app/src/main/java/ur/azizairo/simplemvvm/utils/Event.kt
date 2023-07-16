package ur.azizairo.simplemvvm.utils

/**
 * Represents "side effect".
 * Used in [LiveData] as a wrapper for events.
 */
class Event<T>(
    private val value: T
) {

    private var handled: Boolean = false

    fun getValue(): T? {

        if (handled) return null
        handled = true
        return value
    }
}
