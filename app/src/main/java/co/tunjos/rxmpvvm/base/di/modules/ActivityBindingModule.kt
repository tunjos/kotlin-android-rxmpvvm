package co.tunjos.rxmpvvm.base.di.modules

import co.tunjos.rxmpvvm.MainActivity
import co.tunjos.rxmpvvm.base.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector


/**
 * Abstract Binding module for Activities.
 */
@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity
}
