package co.tunjos.rxmpvvm.extensions.rx

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

fun Lifecycle.addSimpleLifecycleObserver(
    onStart: () -> Unit,
    onStop: () -> Unit,
    onDestroy: () -> Unit
): LifecycleObserver {
    val simpleLifecycleObserver = SimpleLifecycleObserver(onStart, onStop, onDestroy)
    this.addObserver(simpleLifecycleObserver)
    return simpleLifecycleObserver
}

@VisibleForTesting
class SimpleLifecycleObserver(
    private val onStart: () -> Unit,
    private val onStop: () -> Unit,
    private val onDestroy: () -> Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun OnLifecycleEventOnStart() {
        onStart()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun OnLifecycleEventOnStop() {
        onStop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun OnLifecycleEventOnDestroy() {
        onDestroy()
    }
}
