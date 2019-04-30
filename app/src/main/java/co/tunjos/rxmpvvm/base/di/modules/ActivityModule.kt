package co.tunjos.rxmpvvm.base.di.modules

import co.tunjos.rxmpvvm.main.MainActivity
import co.tunjos.rxmpvvm.base.activities.BaseActivity
import co.tunjos.rxmpvvm.base.di.qualifiers.ActivityUiEvents
import co.tunjos.rxmpvvm.base.di.scopes.ActivityScope
import co.tunjos.rxmpvvm.base.mvvm.uievents.UiEvent
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @ActivityScope
    @ActivityUiEvents
    fun provideUiEvents(baseActivity: BaseActivity): Observable<UiEvent> = baseActivity.uiEvents

    @Provides
    @ActivityScope
    fun provideBaseActivity(mainActivity: MainActivity): BaseActivity = mainActivity

}
