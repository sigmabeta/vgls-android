package com.vgleadsheets.di

import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.android.logging.AndroidHatchet
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.BluntHatchet
import net.sigmabeta.sage.logging.BuildConfig
import net.sigmabeta.sage.logging.Hatchet

@BindingContainer
@ContributesTo(AppScope::class)
object LoggingModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideHatchet(): Hatchet = if (BuildConfig.DEBUG) {
        AndroidHatchet()
    } else {
        BluntHatchet()
    }
}
