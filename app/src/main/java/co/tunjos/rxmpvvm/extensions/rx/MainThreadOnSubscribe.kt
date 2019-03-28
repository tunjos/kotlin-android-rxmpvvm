package co.tunjos.rxmpvvm.extensions.rx

import android.view.View
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.MainThreadDisposable

/**
 * An abstract implementation of an [ObservableEmitter] that has uses it's [ObservableEmitter] to emit view events in a
 * cancellation-safe manner using a view of type [V] bound with an model of type [T] using a listener of type [L].
 */
abstract class MainThreadOnSubscribe<V : View, T, L>(private val view: V) : ObservableOnSubscribe<T> {

    override fun subscribe(subscriber: ObservableEmitter<T>) {
        createSubscription(subscriber)

        emitOnSubscriptionCreated(view, subscriber)
    }

    private fun createSubscription(subscriber: ObservableEmitter<T>) {
        val listener = createListener(view, subscriber)

        subscriber.setDisposable(object : MainThreadDisposable() {
            override fun onDispose() {
                removeListener(view, listener)
            }
        })

        addListener(view, listener)
    }

    /**
     * Creates the listener that will be used to listen to view events.
     *
     * @param view The view to be observed.
     * @param emitter The emitter used to emit view events.
     *
     * @return The listener to be used to listen to view events.
     */
    abstract fun createListener(view: V, emitter: ObservableEmitter<T>): L

    /**
     * Adds a listener to a view.
     *
     * @param view The view to be observed.
     * @param listener The listener to be invoked with view events.
     */
    abstract fun addListener(view: V, listener: L)

    /**
     * Removes a listener from a view.
     *
     * @param view The view to be removed.
     * @param listener The listener that was added to be invoked with view events.
     */
    abstract fun removeListener(view: V, listener: L)

    /**
     * Allows emitting view events only when a subscription is created.
     *
     * @param view The view to be observed.
     * @param emitter The emitter used to emit view events.
     */
    abstract fun emitOnSubscriptionCreated(view: V, emitter: ObservableEmitter<T>)
}
