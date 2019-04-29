package co.tunjos.rxmpvvm.base.mvvm.presenteractions

import co.tunjos.rxmpvvm.base.presenters.BaseDisposablePresenter
import io.reactivex.subjects.PublishSubject

/**
 * Represents an action generated from a [BaseDisposablePresenter] and delivered to a UI component(Activity or Fragment)
 * using an emitter [PublishSubject].
 */
interface PresenterAction
