package com.vgleadsheets.di

import com.vgleadsheets.VglsApplication
import com.vgleadsheets.database.di.DatabaseModule
import com.vgleadsheets.main.MainActivity
import com.vgleadsheets.network.di.NetworkModule
import com.vgleadsheets.repository.di.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjectionModule
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
        NetworkModule::class,
        TrackerModule::class]
)
interface AppComponent {
    /**
     * Crucial: injection targets must be the correct type.
     * Passing an interface here will result in a no-op injection.
     */
    fun inject(view: VglsApplication)
    fun inject(view: MainActivity)
}
