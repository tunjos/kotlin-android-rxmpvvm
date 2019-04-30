package co.tunjos.rxmpvvm

import android.app.Activity
import android.app.Application
import co.tunjos.rxmpvvm.base.di.components.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber.DebugTree
import timber.log.Timber.plant
import javax.inject.Inject

class KotlinAndroidRxMpvvmApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerApplicationComponent.builder()
            .create(this)
            .inject(this)

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}
