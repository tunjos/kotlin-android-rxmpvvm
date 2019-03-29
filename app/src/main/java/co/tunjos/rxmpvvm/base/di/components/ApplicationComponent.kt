package co.tunjos.rxmpvvm.base.di.components

import android.content.Context
import co.tunjos.rxmpvvm.KotlinAndroidRxMpvvmApplication
import co.tunjos.rxmpvvm.base.di.modules.ActivityBindingModule
import co.tunjos.rxmpvvm.base.di.modules.ApplicationModule
import co.tunjos.rxmpvvm.base.di.modules.NetworkModule
import co.tunjos.rxmpvvm.base.di.modules.RxSchedulersModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        ActivityBindingModule::class,
        AndroidInjectionModule::class,
        NetworkModule::class,
        RxSchedulersModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<KotlinAndroidRxMpvvmApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<KotlinAndroidRxMpvvmApplication>() {
        abstract override fun build(): ApplicationComponent
    }

    fun exposeContext(): Context
}
