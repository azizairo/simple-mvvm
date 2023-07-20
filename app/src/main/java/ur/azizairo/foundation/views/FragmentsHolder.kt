package ur.azizairo.foundation.views

import ur.azizairo.foundation.ActivityScopeViewModel

interface FragmentsHolder {

    fun notifyScreenUpdates()

    fun getActivityScopeViewModel(): ActivityScopeViewModel
}
