package com.vgleadsheets.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TestAppModule(private val context: Context) {
    @Provides
    @Singleton
    @Named("RunningTest")
    fun provideRunningTest() = true
}
