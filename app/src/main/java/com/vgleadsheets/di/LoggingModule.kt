package com.vgleadsheets.di

import net.sigmabeta.sage.android.logging.AndroidHatchet
import net.sigmabeta.sage.logging.BluntHatchet
import net.sigmabeta.sage.logging.BuildConfig
import net.sigmabeta.sage.logging.Hatchet
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LoggingModule {
    @Provides
    @Singleton
    fun provideHatchet(): Hatchet = if (BuildConfig.DEBUG) {
        AndroidHatchet()
    } else {
        BluntHatchet()
    }
}
