package ur.azizairo.foundation.sideeffects

import ur.azizairo.foundation.model.tasks.dispatchers.Dispatcher
import ur.azizairo.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ur.azizairo.foundation.utils.ResourceActions
import ur.azizairo.foundation.ActivityScopeViewModel

/**
 * Base class for all side-effect mediators.
 * These mediators live in [ActivityScopeViewModel].
 * Mediator should delegate all UI-related logic to the implementation via [target] field.
 */
open class SideEffectMediator<Implementation>(
        dispatcher: Dispatcher = MainThreadDispatcher()
) {

    protected val target = ResourceActions<Implementation>(dispatcher)

    /**
     * Assign/unassign the target implementation for this provider.
     */
    fun setTarget(target: Implementation?) {
        this.target.resource = target
    }

    fun clear() {
        target.clear()
    }

}