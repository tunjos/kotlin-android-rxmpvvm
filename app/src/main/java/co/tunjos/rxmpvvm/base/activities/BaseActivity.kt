package co.tunjos.rxmpvvm.base.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import co.tunjos.rxmpvvm.R
import co.tunjos.rxmpvvm.base.mvvm.uievents.UiEvent
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

abstract class BaseActivity : AppCompatActivity() {

    private val oneTimeUiEvents = PublishSubject.create<UiEvent>()
    private val viewUiEventDisposables = CompositeDisposable()

    val uiEvents: Observable<UiEvent>
        get() = oneTimeUiEvents

    /**
     *
     */
    @LayoutRes
    protected open fun getContentView() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(inflateAndInitContentView())
    }

    private fun inflateAndInitContentView() = View.inflate(this, R.layout.activity_base, null).apply {
        val content = findViewById<ViewGroup>(R.id.content)
        if (getContentView() != 0) {
            View.inflate(this@BaseActivity, getContentView(), content)
        }
    }

    override fun onStart() {
        super.onStart()
        initAndRegisterUiEvents()
    }

    override fun onStop() {
        viewUiEventDisposables.clear()
        super.onStop()
    }

    private fun initAndRegisterUiEvents() {
        val uiEventGenerators = compileViewUiEventGenerators()
        registerViewUiEventGenerators(uiEventGenerators)
    }

    /**
     *
     */
    open fun compileViewUiEventGenerators() = emptyArray<Observable<out UiEvent>>()

    private fun registerViewUiEventGenerators(uiEventGenerators: Array<Observable<out UiEvent>>) {
        for (uiEventGenerator in uiEventGenerators) {
            val disposable = uiEventGenerator.subscribe { event -> manuallyEmitUiEvent(event) }
            viewUiEventDisposables.addAll(disposable)
        }
    }

    /**
     *
     */
    protected fun manuallyEmitUiEvent(event: UiEvent) {
        oneTimeUiEvents.onNext(event)
    }
}
