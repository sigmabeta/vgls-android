package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.downloader.StorageDirectoryProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DownloaderModule {
    @Provides
    @Singleton
    fun provideStorageDirProvider(
        @ApplicationContext context: Context
    ): StorageDirectoryProvider = object : StorageDirectoryProvider {
        override fun getStorageDirectory() = context.filesDir
    }
}
