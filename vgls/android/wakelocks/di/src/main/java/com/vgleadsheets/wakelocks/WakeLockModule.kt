package com.vgleadsheets.wakelocks

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.ui.StringProvider

/**
 * Android wakelock wiring, extracted from the app's ActivityScopedBindingsModule. WakeLockManagerImpl
 * no longer takes an Activity in its constructor; RemasteredActivity binds the current Activity into
 * it at runtime via ActivityGraph.bindActivity. The concrete impl is exposed (VglsAppGraph reads it
 * as a WakeLockManagerImpl for that binding), plus the WakeLockManager interface for consumers.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object WakeLockModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideWakeLockManagerImpl(
        eventDispatcher: EventDispatcher,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        stringProvider: StringProvider,
    ): WakeLockManagerImpl = WakeLockManagerImpl(
        eventDispatcher,
        coroutineScope,
        dispatchers,
        stringProvider,
    )

    @Provides
    @SingleIn(AppScope::class)
    fun provideWakeLockManager(impl: WakeLockManagerImpl): WakeLockManager = impl
}
