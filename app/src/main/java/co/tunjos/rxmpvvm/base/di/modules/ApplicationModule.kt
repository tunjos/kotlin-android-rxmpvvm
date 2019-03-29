package co.tunjos.rxmpvvm.base.di.modules

import android.app.Application
import android.content.Context
import co.tunjos.rxmpvvm.KotlinAndroidRxMpvvmApplication
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjectionModule

@Module(includes = [AndroidInjectionModule::class])
abstract class ApplicationModule {

    @Binds
    abstract fun bindApplication(application: KotlinAndroidRxMpvvmApplication): Application

    @Binds
    abstract fun bindContext(application: KotlinAndroidRxMpvvmApplication): Context
}
