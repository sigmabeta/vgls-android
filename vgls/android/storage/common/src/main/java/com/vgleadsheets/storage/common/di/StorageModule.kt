package com.vgleadsheets.storage.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import net.sigmabeta.sage.coroutines.VglsDispatchers
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.storage.common.AndroidDataStore
import net.sigmabeta.sage.storage.common.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {
    private val Context.dataStore by preferencesDataStore(name = "debug")

    @Provides
    @Singleton
    internal fun provideDebugDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.dataStore

    @Singleton
    @Provides
    fun provideStorage(
        dataStore: DataStore<Preferences>,
        coroutineScope: CoroutineScope,
        dispatchers: VglsDispatchers,
        hatchet: Hatchet
    ): Storage = AndroidDataStore(
        dataStore = dataStore,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
        hatchet = hatchet,
    )
}
