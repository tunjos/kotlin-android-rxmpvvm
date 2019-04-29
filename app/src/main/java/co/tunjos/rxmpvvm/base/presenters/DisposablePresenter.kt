package co.tunjos.rxmpvvm.base.presenters

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.Disposable

/**
 * A presenter that listens to [@OnLifecycleEvent]s [Lifecycle.Event.ON_CREATE] and [Lifecycle.Event.ON_DESTROY] and
 * automatically handles the disposal of subscriptions to reactive event sources here.
 *
 * On calling [register] from the subscribing component's onCreate method, [createSubscriptions] will be invoked
 * automatically after your Component onCreate. [dispose] will be invoked automatically from the subscribed component's
 * onDestroy method.
 */
interface DisposablePresenter {

    /**
     * Subscribe to reactive event sources here. Should call [Disposable.disposeWith] at the end of each reactive
     * subscription.
     */
    fun createSubscriptions()

    /**
     *  Dispose all subscriptions to reactive event sources.
     */
    fun dispose()
}

/**
 * Should be called from subscribing component's onCreate method.
 */
fun DisposablePresenter.register(lifecycle: Lifecycle) {

    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
        throw IllegalStateException("register(lifecycle) should be called in onCreate()")
    }

    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            createSubscriptions()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            lifecycle.removeObserver(this)
            dispose()
        }
    })
}
