package ur.azizairo.foundation.views.activity

/**
 * If you don't want to use [BaseActivity] for some reason for example you have 2 or mode activity
 * hierarchies then you can use this holder instead.
 * Pleas note that you need to call method of [delegate] manually from your activity in this case.
 * See [ActivityDelegate] for details.
 *
 */
interface ActivityDelegateHolder {

    val delegate: ActivityDelegate
}
