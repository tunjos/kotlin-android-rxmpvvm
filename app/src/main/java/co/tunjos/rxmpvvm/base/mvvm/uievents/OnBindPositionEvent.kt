package co.tunjos.rxmpvvm.base.mvvm.uievents

import co.tunjos.rxmpvvm.base.adapters.SimpleDataBindingListAdapter

/**
 * Event sent when a [SimpleDataBindingListAdapter]'s item position is bound.
 */
data class OnBindPositionEvent(val position: Int) : UiEvent
