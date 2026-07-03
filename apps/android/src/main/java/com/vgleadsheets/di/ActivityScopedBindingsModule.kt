package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.offline.WorkManagerOfflineWorkScheduler
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

/**
 * App-level bindings that were `@ActivityScoped` under Hilt. The wakelock wiring moved to
 * :vgls:android:wakelocks:di (WakeLockModule); [OfflineWorkScheduler] stays here until the offline
 * domain is split — it only ever needed the app Context.
 */
@BindingContainer
@ContributesTo(AppScope::class)
object ActivityScopedBindingsModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideOfflineWorkScheduler(context: Context): OfflineWorkScheduler = WorkManagerOfflineWorkScheduler(context)
}
