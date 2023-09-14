package ur.azizairo.foundation.sideeffects.intents.plugin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.sideeffects.intents.Intents

class IntentsSideEffectMediator(
        private val appContext: Context
): SideEffectMediator<Nothing>(), Intents {

    companion object {

        private const val SCHEMA_PACKAGE = "package"
    }

    override fun openAppSettings() {

        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts(SCHEMA_PACKAGE, appContext.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (appContext.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            appContext.startActivity(intent)
        }
    }

}
