package com.vgleadsheets.di

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.FeatureDirectory
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class ViewModelModule {
    @Provides
    @ActivityScoped
    fun provideVMBrainProvider(
        repository: VglsRepository,
        dispatchers: VglsDispatchers,
        urlInfoProvider: UrlInfoProvider,
        stringProvider: StringProvider,
        hatchet: Hatchet,
        selectedPartManager: SelectedPartManager,
    ): BrainProvider =
        FeatureDirectory(
            repository = repository,
            dispatchers = dispatchers,
            urlInfoProvider = urlInfoProvider,
            stringProvider = stringProvider,
            hatchet = hatchet,
            selectedPartManager = selectedPartManager,
        )
}
