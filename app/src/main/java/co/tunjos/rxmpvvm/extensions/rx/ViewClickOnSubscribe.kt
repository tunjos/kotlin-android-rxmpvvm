package co.tunjos.rxmpvvm.extensions.rx

import android.view.View
import co.tunjos.rxmpvvm.base.mvvm.uievents.ViewClickEvent
import io.reactivex.ObservableEmitter
import io.reactivex.android.MainThreadDisposable.verifyMainThread

/**
 * An implementation of a []MainThreadOnSubscribe] transforming [View] clicks sent of a [View.OnClickListener] into
 * [ViewClickEvent]s.
 */
class ViewClickOnSubscribe(view: View) : MainThreadOnSubscribe<View, ViewClickEvent, View.OnClickListener>(view) {

    override fun createListener(
        view: View,
        emitter: ObservableEmitter<ViewClickEvent>
    ): View.OnClickListener {
        return View.OnClickListener { view1 ->

            if (!emitter.isDisposed) {
                verifyMainThread()
                // Emit ViewClickEvents
                emitter.onNext(ViewClickEvent(view1.id))
            }
        }
    }

    override fun addListener(view: View, listener: View.OnClickListener) {
        view.setOnClickListener(listener)
    }

    override fun removeListener(view: View, listener: View.OnClickListener) {
        view.setOnClickListener(null)
    }

    override fun emitOnSubscriptionCreated(view: View, emitter: ObservableEmitter<ViewClickEvent>) {
        // Allows emitting view events only when a subscription is created.
    }
}
