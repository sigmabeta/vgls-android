package com.vgleadsheets.storage.common.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.vgleadsheets.storage.common.AndroidDataStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.storage.common.Storage

@BindingContainer
@ContributesTo(AppScope::class)
object StorageModule {
    private val Context.dataStore by preferencesDataStore(name = "debug")

    @Provides
    @SingleIn(AppScope::class)
    fun provideDebugDataStore(
        context: Context
    ): DataStore<Preferences> = context.dataStore

    @SingleIn(AppScope::class)
    @Provides
    fun provideStorage(
        dataStore: DataStore<Preferences>,
        coroutineScope: CoroutineScope,
        dispatchers: SageDispatchers,
        hatchet: Hatchet
    ): Storage = AndroidDataStore(
        dataStore = dataStore,
        coroutineScope = coroutineScope,
        dispatchers = dispatchers,
        hatchet = hatchet,
    )
}
