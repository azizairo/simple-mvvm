package ur.azizairo.simplemvvm.views

/**
 * Common actions than can be preformed in the view-model
 */
interface UiActions {

    /**
     * Display a simple toast message.
     */
    fun toast(message: String)

    /**
     * Get string resource content by its identifier.
     */
    fun getString(messageRes: Int, vararg args: Any): String
}
