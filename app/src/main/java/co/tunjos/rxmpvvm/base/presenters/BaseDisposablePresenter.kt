package co.tunjos.rxmpvvm.base.presenters

import co.tunjos.rxmpvvm.base.mvvm.presenteractions.PresenterAction
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * A presenter implementing [DisposablePresenter] that uses a [CompositeDisposable] to hold onto and [dispose] off multiple
 * subscriptions/[Disposable]s.
 */
abstract class BaseDisposablePresenter(open val compositeDisposable: CompositeDisposable) :
    DisposablePresenter {

    abstract val presenterActions: Observable<PresenterAction>

    abstract override fun createSubscriptions()

    override fun dispose() {
        compositeDisposable.dispose()
    }
}
