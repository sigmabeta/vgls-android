package com.vgleadsheets.di

import androidx.appcompat.app.AppCompatActivity
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Named

@InstallIn(ActivityComponent::class)
@Module
object DisplayMetricsModule {
    @Provides
    @ActivityScoped
    fun provideWindowMetrics(activity: AppCompatActivity) = WindowMetricsCalculator
        .getOrCreate()
        .computeCurrentWindowMetrics(activity)

    @Provides
    @ActivityScoped
    @Named("WindowHeight")
    fun provideWindowHeight(metrics: WindowMetrics) = metrics.bounds.height()

    @Provides
    @ActivityScoped
    @Named("WindowWidth")
    fun provideWindowWidth(metrics: WindowMetrics) = metrics.bounds.width()
}
