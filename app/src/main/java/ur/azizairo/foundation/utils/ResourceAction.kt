package ur.azizairo.foundation.utils

import ur.azizairo.foundation.model.dispatchers.Dispatcher

typealias ResourceAction<T> = (T) -> Unit

/**
 * Actions queue, where actions are executed only if resource exists. If it doesn't then
 * action is added to queue and waits until resource becomes available.
 */
class ResourceActions<T>(
        private val dispatcher: Dispatcher
) {

    var resource: T? = null
        set(newValue) {
            field = newValue
            if (newValue != null) {
                actions.forEach { action ->
                    dispatcher.dispatch {
                        action.invoke(newValue)
                    }
                }
                actions.clear()
            }
        }

    private val actions = mutableListOf<ResourceAction<T>>()

    operator fun invoke(action: ResourceAction<T>) {

        val resource = this.resource
        if (resource == null) {
            actions += action
        } else {
            dispatcher.dispatch {
                action.invoke(resource)
            }
        }
    }

    fun clear() {

        actions.clear()
    }
}