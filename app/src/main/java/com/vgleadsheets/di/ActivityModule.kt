package com.vgleadsheets.di

import android.content.Context
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.FeatureDirectory
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
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
        dispatchers: VglsDispatchers,
        delayManager: DelayManager,
        appInfo: AppInfo,
        urlInfoProvider: UrlInfoProvider,
        stringProvider: StringProvider,
        hatchet: Hatchet,
        selectedPartManager: SelectedPartManager,
        songRepository: SongRepository,
        gameRepository: GameRepository,
        composerRepository: ComposerRepository,
        randomRepository: RandomRepository,
        favoriteRepository: FavoriteRepository,
        tagRepository: TagRepository,
        homeModuleProvider: HomeModuleProvider,
        generalSettingsManager: GeneralSettingsManager,
        debugSettingsManager: DebugSettingsManager,
    ): BrainProvider =
        FeatureDirectory(
            dispatchers = dispatchers,
            delayManager = delayManager,
            appInfo = appInfo,
            urlInfoProvider = urlInfoProvider,
            stringProvider = stringProvider,
            hatchet = hatchet,
            selectedPartManager = selectedPartManager,
            gameRepository = gameRepository,
            composerRepository = composerRepository,
            songRepository = songRepository,
            randomRepository = randomRepository,
            favoriteRepository = favoriteRepository,
            tagRepository = tagRepository,
            homeModuleProvider = homeModuleProvider,
            generalSettingsManager = generalSettingsManager,
            debugSettingsManager = debugSettingsManager,
        )
}
