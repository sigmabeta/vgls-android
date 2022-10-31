package com.vgleadsheets.storage.di

import android.content.Context
import com.uber.simplestore.SimpleStore
import com.uber.simplestore.impl.SimpleStoreFactory
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.storage.SimpleStorage
import com.vgleadsheets.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Singleton
    @Provides
    fun provideSimpleStore(context: Context) = SimpleStoreFactory.create(
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
