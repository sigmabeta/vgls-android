package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.model.time.ThreeTenTime
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideContext() = context

    @Provides
    @Singleton
    fun provideTime(context: Context): ThreeTenTime = ThreeTenTime.Impl(context)

    @Provides
    @Singleton
    @Named("RunningTest")
    @Suppress("FunctionOnlyReturningConstant")
    fun provideRunningTest() = false
}
