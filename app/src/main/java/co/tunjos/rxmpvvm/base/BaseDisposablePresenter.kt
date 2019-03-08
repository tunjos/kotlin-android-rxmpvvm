package co.tunjos.rxmpvvm.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * A presenter implementing [DisposablePresenter] that uses a [CompositeDisposable] to hold onto and [dispose] off multiple
 * subscriptions/[Disposable]s.
 */
class BaseDisposablePresenter(val compositeDisposable: CompositeDisposable) : DisposablePresenter {

    override fun createSubscriptions() {

    }

    override fun dispose() {
        compositeDisposable.dispose()
    }
}
