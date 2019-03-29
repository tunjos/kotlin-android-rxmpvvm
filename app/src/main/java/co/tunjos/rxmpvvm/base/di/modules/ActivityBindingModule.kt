package co.tunjos.rxmpvvm.base.di.modules

import co.tunjos.rxmpvvm.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Abstract Binding module for Activities.
 */
@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = [ActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity
}
