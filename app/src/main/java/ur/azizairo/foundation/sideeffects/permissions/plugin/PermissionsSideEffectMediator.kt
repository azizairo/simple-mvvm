package ur.azizairo.foundation.sideeffects.permissions.plugin

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import ur.azizairo.foundation.model.ErrorResult
import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.model.tasks.callback.CallbackTask
import ur.azizairo.foundation.model.tasks.callback.Emitter
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.sideeffects.permissions.Permissions

class PermissionsSideEffectMediator(
        private val appContext: Context
): SideEffectMediator<PermissionsSideEffectImpl>(), Permissions {

    var retainedState = RetainedState()

    override fun hasPermission(permission: String): Boolean {

        return ContextCompat.checkSelfPermission(appContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun requestPermission(
            permission: String
    ): PermissionStatus = CallbackTask.create<PermissionStatus> { emitter ->

        if (retainedState.emmitter != null) {
            emitter.emit(ErrorResult(IllegalStateException("Only one permission request can be active")))
            return@create
        }
        retainedState.emmitter = emitter
        target { implementation ->
            implementation.requestPermission(permission)
        }
    }.suspend()

    class RetainedState(
        var emmitter: Emitter<PermissionStatus>? = null
    )
}