package com.vgleadsheets.di

import com.vgleadsheets.VglsApplication
import com.vgleadsheets.conversion.android.di.ConverterModule
import com.vgleadsheets.conversion.android.di.DataSourceModule
import com.vgleadsheets.coroutines.DispatcherModule
import com.vgleadsheets.database.android.di.DatabaseModule
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.repository.di.RepositoryModule
import com.vgleadsheets.resources.di.ResourcesModule
import com.vgleadsheets.storage.di.StorageModule
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
        ActivityBindingModule::class,
        RepositoryModule::class,
        ResourcesModule::class,
        DatabaseModule::class,
        ConverterModule::class,
        DataSourceModule::class,
        DispatcherModule::class,
        ApiModule::class,
        NetworkModule::class,
        StorageModule::class,
        TrackerModule::class,
        PerfTrackingModule::class,
        PerfBackendModule::class,
        LoggingModule::class
    ]
)
interface AppComponent : AndroidInjector<VglsApplication> {
    @Component.Factory
    @Suppress("UnnecessaryAbstractClass")
    abstract class Factory {
        abstract fun create(appModule: AppModule): AppComponent
    }

    /**
     * Crucial: injection targets must be the correct type.
     * Passing an interface here will result in a no-op injection.
     */
    fun inject(view: MainActivity)
}
