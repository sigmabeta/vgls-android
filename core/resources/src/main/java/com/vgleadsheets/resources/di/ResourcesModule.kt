package com.vgleadsheets.resources.di

import android.content.Context
import android.content.res.Resources
import com.vgleadsheets.resources.RealResourceProvider
import com.vgleadsheets.resources.ResourceProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ResourcesModule {
    @Provides
    @Singleton
    fun provideResources(context: Context) = context.resources

    @Provides
    @Singleton
    fun provideResourceProvider(resources: Resources): ResourceProvider =
        RealResourceProvider(resources)
}
