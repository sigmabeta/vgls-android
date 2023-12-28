package com.vgleadsheets.di

import com.vgleadsheets.MockStorage
import com.vgleadsheets.storage.Storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MockStorageModule {
    @Singleton
    @Provides
    fun provideStorage(): Storage = MockStorage()
}
