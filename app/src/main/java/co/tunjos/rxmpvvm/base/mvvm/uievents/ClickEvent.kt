package co.tunjos.rxmpvvm.base.mvvm.uievents

/**
 * Android Widget click [UiEvent]s.
 */
interface ClickEvent : UiEvent {

    /**
     * The view's identifier: An integer used to identify the view.
     */
    val id: Int
}
