package com.vgleadsheets.di

import com.vgleadsheets.MockStorage
import com.vgleadsheets.storage.Storage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MockStorageModule {
    @Singleton
    @Provides
    fun provideStorage(): Storage = MockStorage()
}
