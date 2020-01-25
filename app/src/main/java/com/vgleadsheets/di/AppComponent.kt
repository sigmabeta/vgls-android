package com.vgleadsheets.di

import com.vgleadsheets.VglsApplication
import com.vgleadsheets.database.di.DatabaseModule
import com.vgleadsheets.images.di.ImageModule
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.network.di.NetworkModule
import com.vgleadsheets.repository.di.RepositoryModule
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
        AssistedInjectionModules::class,
        ActivityBindingModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ImageModule::class,
        NetworkModule::class,
        StorageModule::class,
        TrackerModule::class]
)
interface AppComponent: AndroidInjector<VglsApplication> {
    @Component.Factory
    abstract class Factory {
        abstract fun create(appModule: AppModule): AppComponent
    }

    /**
     * Crucial: injection targets must be the correct type.
     * Passing an interface here will result in a no-op injection.
     */
    fun inject(view: MainActivity)
}
