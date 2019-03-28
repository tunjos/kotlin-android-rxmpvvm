package co.tunjos.rxmpvvm.base.mvvm.uievents

import android.view.View
import androidx.annotation.IdRes

/**
 * Android [View] click [UiEvent]s.
 */
open class ViewClickEvent(@IdRes override val id: Int) : ClickEvent
