package ur.azizairo.foundation.sideeffects.navigator.plugin

import ur.azizairo.foundation.sideeffects.navigator.Navigator
import ur.azizairo.foundation.sideeffects.SideEffectMediator
import ur.azizairo.foundation.views.BaseScreen

class NavigatorSideEffectMediator: SideEffectMediator<Navigator>() , Navigator {

    override fun launch(screen: BaseScreen) = target {

        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {

        it.goBack(result)
    }

}
