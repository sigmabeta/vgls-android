package com.vgleadsheets.di

import androidx.appcompat.app.AppCompatActivity
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class DisplayMetricsModule {
    @Provides
    @ActivityScope
    fun provideWindowMetrics(activity: AppCompatActivity) = WindowMetricsCalculator
        .getOrCreate()
        .computeCurrentWindowMetrics(activity)

    @Provides
    @ActivityScope
    @Named("WindowHeight")
    fun provideWindowHeight(metrics: WindowMetrics) = metrics.bounds.height()

    @Provides
    @ActivityScope
    @Named("WindowWidth")
    fun provideWindowWidth(metrics: WindowMetrics) = metrics.bounds.width()
}

