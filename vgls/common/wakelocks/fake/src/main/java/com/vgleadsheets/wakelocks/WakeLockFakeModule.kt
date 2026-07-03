package com.vgleadsheets.wakelocks

import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

/** Binds the no-op WakeLockManager — used by platforms without a real wakelock (the desktop app). */
@BindingContainer
@ContributesTo(AppScope::class)
object WakeLockFakeModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideWakeLockManager(): WakeLockManager = NoopWakeLockManager()
}
