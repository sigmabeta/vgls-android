package com.vgleadsheets.features

import com.vgleadsheets.analytics.VglsAnalytics
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.offline.OfflineWorkScheduler
import com.vgleadsheets.remaster.browse.BrowseViewModelBrain
import com.vgleadsheets.remaster.composers.detail.ComposerDetailViewModelBrain
import com.vgleadsheets.remaster.composers.list.ComposerListViewModelBrain
import com.vgleadsheets.remaster.difficulty.list.DifficultyListViewModelBrain
import com.vgleadsheets.remaster.difficulty.values.DifficultyValuesViewModelBrain
import com.vgleadsheets.remaster.favorites.FavoritesViewModelBrain
import com.vgleadsheets.remaster.games.detail.GameDetailViewModelBrain
import com.vgleadsheets.remaster.games.list.GameListViewModelBrain
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.remaster.home.HomeViewModelBrain
import com.vgleadsheets.remaster.menu.MenuViewModelBrain
import com.vgleadsheets.remaster.offline.content.OfflineContentViewModelBrain
import com.vgleadsheets.remaster.offline.updates.OfflineUpdatesViewModelBrain
import com.vgleadsheets.remaster.parts.PartsListViewModelBrain
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain
import com.vgleadsheets.remaster.songs.list.SongListViewModelBrain
import com.vgleadsheets.remaster.tags.list.TagListViewModelBrain
import com.vgleadsheets.remaster.tags.songs.TagValueSongsViewModelBrain
import com.vgleadsheets.remaster.tags.values.TagValuesViewModelBrain
import com.vgleadsheets.remaster.updates.UpdatesViewModelBrain
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.DbUpdater
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.repository.history.SongHistoryRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope
import net.sigmabeta.sage.appinfo.AppInfo
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.list.BrainProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.RouteDescriptor
import net.sigmabeta.sage.settings.DebugSettingsManager
import net.sigmabeta.sage.settings.GeneralSettingsManager
import net.sigmabeta.sage.time.ThreeTenTime
import net.sigmabeta.sage.ui.StringProvider

class FeatureDirectory(
    private val dbUpdater: DbUpdater,
    private val songHistoryRepository: SongHistoryRepository,
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val randomRepository: RandomRepository,
    private val favoriteRepository: FavoriteRepository,
    private val offlineRepository: OfflineRepository,
    private val tagRepository: TagRepository,
    private val dispatchers: SageDispatchers,
    private val delayManager: DelayManager,
    private val appInfo: AppInfo,
    private val urlInfoProvider: UrlInfoProvider,
    private val analytics: VglsAnalytics,
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val threeTenTime: ThreeTenTime,
    private val selectedPartManager: SelectedPartManager,
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    private val userContentGenerator: UserContentGenerator,
    private val userContentMigrator: UserContentMigrator,
    private val homeModuleProvider: HomeModuleProvider,
    private val offlineWorkScheduler: OfflineWorkScheduler,
) : BrainProvider {
    @Suppress("LongMethod")
    override fun provideBrain(
        destination: RouteDescriptor,
        coroutineScope: CoroutineScope
    ): ListViewModelBrain {
        @Suppress("UNCHECKED_CAST")
        destination as Destination
        val scheduler = ViewModelScheduler(
            coroutineScope,
            dispatchers,
            delayManager,
        )

        return when (destination) {
            Destination.HOME -> HomeViewModelBrain(
                stringProvider,
                analytics,
                hatchet,
                scheduler,
                homeModuleProvider,
                tagRepository,
                randomRepository,
                threeTenTime,
            )

            Destination.BROWSE -> BrowseViewModelBrain(
                tagRepository,
                analytics,
                stringProvider,
                hatchet,
                scheduler,
            )

            Destination.PART_PICKER -> PartsListViewModelBrain(
                stringProvider,
                hatchet,
                analytics,
                scheduler,
                selectedPartManager,
            )

            Destination.GAME_DETAIL -> GameDetailViewModelBrain(
                songRepository,
                gameRepository,
                composerRepository,
                favoriteRepository,
                offlineRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.GAMES_LIST -> GameListViewModelBrain(
                gameRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSER_DETAIL -> ComposerDetailViewModelBrain(
                songRepository,
                composerRepository,
                gameRepository,
                favoriteRepository,
                offlineRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSERS_LIST -> ComposerListViewModelBrain(
                composerRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.SONG_DETAIL -> SongDetailViewModelBrain(
                songRepository,
                gameRepository,
                composerRepository,
                favoriteRepository,
                offlineRepository,
                tagRepository,
                scheduler,
                urlInfoProvider,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.SONGS_LIST -> SongListViewModelBrain(
                songRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.FAVORITES -> FavoritesViewModelBrain(
                favoriteRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.DIFFICULTY_LIST -> DifficultyListViewModelBrain(
                tagRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.DIFFICULTY_VALUES_LIST -> DifficultyValuesViewModelBrain(
                tagRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.TAGS_LIST -> TagListViewModelBrain(
                tagRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.TAGS_VALUES_LIST -> TagValuesViewModelBrain(
                tagRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.TAGS_VALUES_SONG_LIST -> TagValueSongsViewModelBrain(
                tagRepository,
                songRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet
            )

            Destination.MENU -> MenuViewModelBrain(
                dbUpdater,
                songHistoryRepository,
                generalSettingsManager,
                debugSettingsManager,
                userContentGenerator,
                userContentMigrator,
                appInfo,
                threeTenTime,
                analytics,
                stringProvider,
                hatchet,
                scheduler,
                offlineWorkScheduler,
            )

            Destination.UPDATES -> UpdatesViewModelBrain(
                stringProvider,
                hatchet,
                analytics,
                scheduler,
            )

            Destination.OFFLINE -> OfflineContentViewModelBrain(
                offlineRepository,
                scheduler,
                analytics,
                stringProvider,
                hatchet,
            )

            Destination.OFFLINE_UPDATES -> OfflineUpdatesViewModelBrain(
                offlineRepository,
                threeTenTime,
                stringProvider,
                hatchet,
                analytics,
                scheduler,
            )

            Destination.NONE,
            Destination.SEARCH,
            Destination.SONG_VIEWER,
            Destination.LICENSES -> throw IllegalArgumentException("Not a list view: $destination")
        }
    }
}
