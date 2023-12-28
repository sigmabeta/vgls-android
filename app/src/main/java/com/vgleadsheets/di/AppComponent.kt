package com.vgleadsheets.di

import com.vgleadsheets.conversion.android.di.ConverterModule
import com.vgleadsheets.conversion.android.di.DataSourceModule
import com.vgleadsheets.coroutines.DispatcherModule
import com.vgleadsheets.database.android.di.DatabaseModule
import com.vgleadsheets.repository.di.RepositoryModule
import com.vgleadsheets.resources.di.ResourcesModule
import com.vgleadsheets.storage.di.StorageModule
import com.vgleadsheets.tracking.TrackerModule
import dagger.Component

@Component(
    modules = [
        AppModule::class,
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
interface AppComponent
