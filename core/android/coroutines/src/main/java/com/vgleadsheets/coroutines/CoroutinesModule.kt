package com.vgleadsheets.coroutines

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {
    @Singleton
    @Provides
    fun provideCoroutineScope(vglsDispatchers: VglsDispatchers) = CoroutineScope(vglsDispatchers.computation)

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
