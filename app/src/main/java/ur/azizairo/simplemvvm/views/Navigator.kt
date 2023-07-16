package ur.azizairo.simplemvvm.views

import ur.azizairo.simplemvvm.views.base.BaseScreen

/**
 * Navigation for your application
 */
interface Navigator {

    /**
     * Launch a new screen at the top of back stack
     */
    fun launch(screen: BaseScreen)


    /**
     * Go back to the previous and optionally send some results.
     */
    fun goBack(result: Any? = null)
}
