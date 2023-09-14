package ur.azizairo.foundation.sideeffects.permissions.plugin

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import ur.azizairo.foundation.model.SuccessResult
import ur.azizairo.foundation.sideeffects.SideEffectImplementation
import ur.azizairo.foundation.sideeffects.permissions.plugin.PermissionsSideEffectMediator.RetainedState

class PermissionsSideEffectImpl(
        private val retainedState: RetainedState
): SideEffectImplementation() {

    fun requestPermission(permission: String) {

        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            granted: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, granted)
        val emitter = retainedState.emmitter ?: return
        if (requestCode == REQUEST_CODE) {
            retainedState.emmitter = null
            if (granted[0] == PackageManager.PERMISSION_GRANTED) {
                emitter.emit(SuccessResult(PermissionStatus.GRANTED))
            } else {
                val showRationale = requireActivity().shouldShowRequestPermissionRationale(permissions[0])
                if (showRationale) {
                    emitter.emit(SuccessResult(PermissionStatus.DENIED))
                } else {
                    emitter.emit(SuccessResult(PermissionStatus.DENIED_FOREVER))
                }
            }

        }
    }

    private companion object {

        const val REQUEST_CODE = 1100
    }
}