package co.tunjos.rxmpvvm.base.di.modules

import co.tunjos.rxmpvvm.base.di.qualifiers.ComputationScheduler
import co.tunjos.rxmpvvm.base.di.qualifiers.IoScheduler
import co.tunjos.rxmpvvm.base.di.qualifiers.NewThreadScheduler
import co.tunjos.rxmpvvm.base.di.qualifiers.UiScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
class RxSchedulersModule {

    @Provides
    @Singleton
    @IoScheduler
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Singleton
    @UiScheduler
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Singleton
    @ComputationScheduler
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Singleton
    @NewThreadScheduler
    fun provideNewThreadScheduler(): Scheduler = Schedulers.newThread()
}
