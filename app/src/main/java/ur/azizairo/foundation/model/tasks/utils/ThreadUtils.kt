package ur.azizairo.foundation.model.tasks.utils

/**
 * Common methods for working with threads.
 */
interface ThreadUtils {

    /**
     * Suspend the current thread for the specified amount of time.
     */
    fun sleep(millis: Long)

    class DefaultThreadUtils: ThreadUtils {

        override fun sleep(millis: Long) {

            Thread.sleep(millis)
        }
    }

}
