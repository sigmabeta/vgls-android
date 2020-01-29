package com.vgleadsheets.di

import com.vgleadsheets.UiTest
import com.vgleadsheets.UiTestApplication
import com.vgleadsheets.database.di.DatabaseModule
import com.vgleadsheets.images.di.ImageModule
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.repository.di.RepositoryModule
import com.vgleadsheets.tracking.TrackerModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        AssistedInjectionModules::class,
        ActivityBindingModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ImageModule::class,
        MockApiModule::class,
        NetworkModule::class,
        MockStorageModule::class,
        TrackerModule::class]
)
interface UiTestAppComponent : AndroidInjector<UiTestApplication> {
    @Component.Factory
    abstract class Factory {
        abstract fun create(appModule: AppModule): UiTestAppComponent
    }

    /**
     * Crucial: injection targets must be the correct type.
     * Passing an interface here will result in a no-op injection.
     */
    fun inject(view: MainActivity)

    fun inject(test: UiTest)
}
