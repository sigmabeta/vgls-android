package com.vgleadsheets.storage.di

import android.content.Context
import com.uber.simplestore.SimpleStore
import com.uber.simplestore.impl.SimpleStoreFactory
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.storage.SimpleStorage
import com.vgleadsheets.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object StorageModule {
    @Singleton
    @Provides
    fun provideSimpleStore(
        @ApplicationContext context: Context
    ): SimpleStore = SimpleStoreFactory.create(
        context,
        "vgls-store"
    )

    @Singleton
    @Provides
    fun provideStorage(
        store: SimpleStore,
        dispatchers: VglsDispatchers
    ): Storage = SimpleStorage(store, dispatchers)
}
