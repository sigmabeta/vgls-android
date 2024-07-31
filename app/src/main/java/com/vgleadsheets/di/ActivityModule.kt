package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.FeatureDirectory
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.StringResources
import com.vgleadsheets.urlinfo.UrlInfoProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {
    @Provides
    @ActivityScoped
    fun provideStringProvider(@ActivityContext context: Context): StringProvider {
        return StringResources(context.resources)
    }

    @Provides
    @ActivityScoped
    @Suppress("LongParameterList")
    fun provideVMBrainProvider(
        repository: VglsRepository,
        dispatchers: VglsDispatchers,
        urlInfoProvider: UrlInfoProvider,
        stringProvider: StringProvider,
        hatchet: Hatchet,
        selectedPartManager: SelectedPartManager,
        songRepository: SongRepository,
        gameRepository: GameRepository,
        composerRepository: ComposerRepository,
        randomRepository: RandomRepository,
        favoriteRepository: FavoriteRepository,
        homeModuleProvider: HomeModuleProvider,
    ): BrainProvider =
        FeatureDirectory(
            repository = repository,
            dispatchers = dispatchers,
            urlInfoProvider = urlInfoProvider,
            stringProvider = stringProvider,
            hatchet = hatchet,
            selectedPartManager = selectedPartManager,
            gameRepository = gameRepository,
            composerRepository = composerRepository,
            songRepository = songRepository,
            randomRepository = randomRepository,
            favoriteRepository = favoriteRepository,
            homeModuleProvider = homeModuleProvider,
        )
}
