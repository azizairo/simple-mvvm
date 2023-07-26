package ur.azizairo.foundation.model.tasks

interface ThreadUtils {

    fun sleep(millis: Long)

    class DefaultThreadUtils: ThreadUtils {

        override fun sleep(millis: Long) {

            Thread.sleep(millis)
        }
    }

}
