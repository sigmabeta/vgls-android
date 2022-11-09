package com.vgleadsheets.coroutines

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class DispatcherModule {
    @Singleton
    @Provides
    fun provideDispatchers() =
        VglsDispatchers(
            computation = Dispatchers.Default,
            database = Dispatchers.IO,
            disk = Dispatchers.IO,
            network = Dispatchers.IO,
            main = Dispatchers.Main
        )
}
