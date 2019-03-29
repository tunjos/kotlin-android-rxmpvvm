package co.tunjos.rxmpvvm.base.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class ActivityModule {

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()
}

