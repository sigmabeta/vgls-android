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
    internal fun provideCoroutineScope(
        vglsDispatchers: VglsDispatchers
    ) = CoroutineScope(vglsDispatchers.computation)

    @Singleton
    @Provides
    internal fun provideRegularDispatchers() = VglsDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )
}
