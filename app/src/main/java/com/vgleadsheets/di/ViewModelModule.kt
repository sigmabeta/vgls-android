package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.FeatureDirectory
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ViewModelModule {
    @Provides
    @Singleton
    fun provideVMBrainProvider(
        repository: VglsRepository,
        dispatchers: VglsDispatchers,
        urlInfoProvider: UrlInfoProvider
    ): BrainProvider =
        FeatureDirectory(
            repository = repository,
            dispatchers = dispatchers,
            urlInfoProvider = urlInfoProvider,
        )
}
