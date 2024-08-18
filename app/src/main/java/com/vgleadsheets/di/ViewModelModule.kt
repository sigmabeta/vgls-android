package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.dispatchers.DelayManagerImpl
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope

@InstallIn(ViewModelComponent::class)
@Module
class ViewModelModule {
    @Provides
    @ViewModelScoped
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return StringResources(context.resources)
    }

    @Provides
    @ViewModelScoped
    fun provideDelayManager(
        settingsManager: DebugSettingsManager,
        dispatchers: VglsDispatchers,
        coroutineScope: CoroutineScope
    ): DelayManager {
        return DelayManagerImpl(
            settingsManager = settingsManager,
            dispatchers = dispatchers,
            coroutineScope = coroutineScope,
        )
    }
}
