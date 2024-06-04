package com.vgleadsheets.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {
    @Singleton
    @Provides
    fun provideCoroutineScope(vglsDispatchers: VglsDispatchers) = CoroutineScope(vglsDispatchers.computation)

    @Singleton
    @Provides
    @Suppress("InjectDispatcher")
    fun provideDispatchers() =
        VglsDispatchers(
            computation = Dispatchers.Default,
            database = Dispatchers.IO,
            disk = Dispatchers.IO,
            network = Dispatchers.IO,
            main = Dispatchers.Main
        )
}
