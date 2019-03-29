package co.tunjos.rxmpvvm.extensions.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Adds this [Disposable] to the [CompositeDisposable] container.
 */
fun Disposable.disposeWith(compositeDisposable: CompositeDisposable): Disposable {
    compositeDisposable.add(this)
    return this
}
