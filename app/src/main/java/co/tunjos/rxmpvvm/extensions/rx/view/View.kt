package co.tunjos.rxmpvvm.extensions.rx.view

import android.view.View
import co.tunjos.rxmpvvm.base.mvvm.uievents.ViewClickEvent
import co.tunjos.rxmpvvm.extensions.rx.ViewClickOnSubscribe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

const val VIEW_CLICK_THROTTLE_WINDOW_DURATION = 500L

fun View.click(): Observable<ViewClickEvent> = clickRaw().throttleFirst(
    VIEW_CLICK_THROTTLE_WINDOW_DURATION,
    TimeUnit.MILLISECONDS,
    AndroidSchedulers.mainThread()
)

fun View.clickRaw(): Observable<ViewClickEvent> = Observable.create(ViewClickOnSubscribe(this))
