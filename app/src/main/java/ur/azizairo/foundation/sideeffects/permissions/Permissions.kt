package ur.azizairo.foundation.sideeffects.permissions

import ur.azizairo.foundation.model.tasks.Task
import ur.azizairo.foundation.sideeffects.permissions.plugin.PermissionStatus
import ur.azizairo.foundation.sideeffects.permissions.plugin.PermissionsPlugin

/**
 * Side-effect interface for managing permissions from view-model.
 * You need to add [PermissionsPlugin] to your activity before using this feature.
 *
 * WARNING! Pleas note that such usage of permission requests doesn't allow you to handle
 * responses after app killing.
 */
interface Permissions {

    /**
     * Whether the app has the specified permission or not.
     */
    fun hasPermission(permission: String): Boolean

    /**
     * Request the specified permission.
     * See [PermissionStatus]
     */
    fun requestPermission(permission: String): Task<PermissionStatus>
}
