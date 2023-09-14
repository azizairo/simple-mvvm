package ur.azizairo.foundation.sideeffects.resources.plugin

import android.content.Context
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.sideeffects.resources.Resources

class ResourcesSideEffectMediator(
        private val appContext: Context
): SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {

        return appContext.getString(resId, *args)
    }
}
