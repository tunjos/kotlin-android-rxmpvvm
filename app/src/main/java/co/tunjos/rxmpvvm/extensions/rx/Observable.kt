package co.tunjos.rxmpvvm.extensions.rx

import androidx.lifecycle.Lifecycle
import io.reactivex.Notification
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.observables.ConnectableObservable

/**
 * Subscribes to an [Observable] which will only receive events between the onStart and onStop lifecycle.Events received
 * after onStop lifecycle are cached and not delivered until a new onStart lifecycle. Un-subscription occurs
 * automatically in the onDestroy lifecycle.
 */
fun <T> Observable<T>.subscribe(lifecycle: Lifecycle, onNext: (T) -> Unit): Disposable =
    subscribeAndResubscribeAutomatically(lifecycle, this.publish()) {
        it.subscribe(
            onNext
        )
    }

/**
 * Subscribes to an [Observable] which will only receive events between the onStart and onStop lifecycle.Events received
 * after onStop lifecycle are cached and not delivered until a new onStart lifecycle. Un-subscription occurs
 * automatically in the onDestroy lifecycle.
 */
fun <T> Observable<T>.subscribe(lifecycle: Lifecycle, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable =
    subscribeAndResubscribeAutomatically(lifecycle, this.publish()) {
        it.subscribe(
            onNext,
            onError
        )
    }

/**
 * Subscribes to an [Observable] which will only receive events between the onStart and onStop lifecycle.Events received
 * after onStop lifecycle are cached and not delivered until a new onStart lifecycle. Un-subscription occurs
 * automatically in the onDestroy lifecycle.
 */
fun <T> Observable<T>.subscribe(
    lifecycle: Lifecycle,
    onNext: (T) -> Unit,
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit
): Disposable =
    subscribeAndResubscribeAutomatically(lifecycle, this.publish()) {
        it.subscribe(
            onNext,
            onError,
            onComplete
        )
    }

private fun <T> subscribeAndResubscribeAutomatically(
    lifecycle: Lifecycle,
    connectableSource: ConnectableObservable<T>,
    subscriptionFunction: (Observable<T>) -> Disposable
): Disposable {
    val mainDisposable = connectableSource.connect()
    var connectibleSourceDisposable = subscriptionFunction(connectableSource)
    var cacheDisposable: Disposable? = null

    val undeliveredEventsCache = ArrayList<Notification<T>>()

    lifecycle.addSimpleLifecycleObserver(
        onStart = {
            connectibleSourceDisposable.dispose()
            connectibleSourceDisposable = if (undeliveredEventsCache.firstOrNull { it.isOnComplete } != null) {
                // Observable completed after onStop lifecycle, resend cached events until completion
                subscriptionFunction(Observable.fromIterable(undeliveredEventsCache).dematerialize())
            } else {
                // Observable didn't complete after onStop lifecycle, resend cached events cached and keep on listening
                subscriptionFunction(
                    connectableSource.startWith(Observable.fromIterable(undeliveredEventsCache).dematerialize())
                )
            }
            undeliveredEventsCache.clear()
            cacheDisposable?.dispose()
        },
        onStop = {
            connectibleSourceDisposable.dispose()
            cacheDisposable = connectableSource.materialize().subscribe { undeliveredEventsCache.add(it) }
        },
        onDestroy = {
            mainDisposable.dispose()
            cacheDisposable?.dispose()
        }
    )
    return mainDisposable
}
