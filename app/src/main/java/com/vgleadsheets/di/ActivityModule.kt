package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    @ActivityScoped
    fun provideStringProvider(@ActivityContext context: Context): StringProvider {
        return StringResources(context.resources)
    }
}
