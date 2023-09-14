package ur.azizairo.foundation.sideeffects.resources

import androidx.annotation.StringRes
import ur.azizairo.foundation.sideeffects.resources.plugin.ResourcesPlugin

/**
 * Interface for accessing resources from view-models.
 * You need to add [ResourcesPlugin] yo your activity befor using this feature.
 */
interface Resources {

    fun getString(@StringRes resId: Int, vararg args: Any): String

}
