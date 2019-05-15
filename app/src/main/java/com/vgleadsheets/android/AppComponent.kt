package com.vgleadsheets.android

import com.vgleadsheets.network.di.NetworkModule
import com.vgleadsheets.repository.di.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class, NetworkModule::class]
)
interface AppComponent {
    /**
     * Crucial: injection targets must be the correct type.
     * Passing an interface here will result in a no-op injection.
     */
    fun inject(view: MainActivity)
}