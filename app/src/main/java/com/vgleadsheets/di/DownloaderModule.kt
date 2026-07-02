package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.downloader.StorageDirectoryProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import net.sigmabeta.sage.di.AppScope

@BindingContainer
@ContributesTo(AppScope::class)
object DownloaderModule {
    @Provides
    @SingleIn(AppScope::class)
    fun provideStorageDirProvider(
        context: Context
    ): StorageDirectoryProvider = object : StorageDirectoryProvider {
        override fun getStorageDirectory() = context.filesDir
    }
}
