package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.offline.WorkManagerOfflineWorkScheduler
import com.vgleadsheets.wakelocks.WakeLockManager
import com.vgleadsheets.wakelocks.WakeLockManagerImpl
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
 * Bindings that were `@ActivityScoped` under Hilt's old `ActivityModule`. The Metro graph is
 * single-scope (AppScope) with no activity component, so these are promoted to `AppScope`:
 *  - [WakeLockManagerImpl] no longer takes an `Activity` in its constructor; `RemasteredActivity`
 *    binds the current Activity into it at runtime via `ActivityGraph.bindActivity`.
 *  - [OfflineWorkScheduler] only ever needed the (app) Context.
 * The former `BrainProvider` binding moved to `FeatureDirectory` itself (`@ContributesBinding`).
 */
@BindingContainer
@ContributesTo(AppScope::class)
object ActivityScopedBindingsModule {
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
    fun provideWakeLockManager(impl: WakeLockManagerImpl): WakeLockManager = impl

    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineWorkScheduler(context: Context): OfflineWorkScheduler =
        WorkManagerOfflineWorkScheduler(context)
}
