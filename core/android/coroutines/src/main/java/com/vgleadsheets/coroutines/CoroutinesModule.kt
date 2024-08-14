package com.vgleadsheets.coroutines

import com.vgleadsheets.coroutines.VglsDispatchers.Companion.DEP_NAME_DISPATCHER_DELAY
import com.vgleadsheets.coroutines.VglsDispatchers.Companion.DEP_NAME_DISPATCHER_REGULAR
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
object CoroutinesModule {
    @Singleton
    @Provides
    internal fun provideCoroutineScope(
        @Named(DEP_NAME_DISPATCHER_REGULAR) vglsDispatchers: VglsDispatchers
    ) = CoroutineScope(vglsDispatchers.computation)

    @Singleton
    @Provides
    @Named(DEP_NAME_DISPATCHER_DELAY)
    internal fun provideDelayDispatchers() = VglsDispatchers(
        computation = SyntheticDelayDispatcher(Dispatchers.Default),
        disk = SyntheticDelayDispatcher(Dispatchers.IO),
        network = SyntheticDelayDispatcher(Dispatchers.IO),
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    @Named(DEP_NAME_DISPATCHER_REGULAR)
    internal fun provideRegularDispatchers() = VglsDispatchers(
        computation = Dispatchers.Default,
        disk = Dispatchers.IO,
        network = Dispatchers.IO,
        main = Dispatchers.Main
    )

    @Singleton
    @Provides
    // @Named(DEP_NAME_DISPATCHER_VGLS)
    @Suppress("InjectDispatcher")
    internal fun provideDispatchers(
        provider: DispatcherConfigProvider,
        @Named(DEP_NAME_DISPATCHER_DELAY) delayDispatchers: VglsDispatchers,
        @Named(DEP_NAME_DISPATCHER_REGULAR) regularDispatchers: VglsDispatchers,
    ): VglsDispatchers {
        println("Providing dispatchers...")
        return if (provider.shouldUseDelayDispatcher()) {
            println("Providing delay dispatchers")
            delayDispatchers
        } else {
            println("Providing regular dispatchers")
            regularDispatchers
        }
    }
}
