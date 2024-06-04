package com.vgleadsheets.resources.di

import android.content.Context
import android.content.res.Resources
import com.vgleadsheets.resources.RealResourceProvider
import com.vgleadsheets.resources.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ResourcesModule {
    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    @Singleton
    fun provideResourceProvider(resources: Resources): ResourceProvider =
        RealResourceProvider(resources)
}
