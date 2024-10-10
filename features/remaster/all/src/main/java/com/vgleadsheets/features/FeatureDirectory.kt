package com.vgleadsheets.features

import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
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
import com.vgleadsheets.remaster.parts.PartsListViewModelBrain
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain
import com.vgleadsheets.remaster.songs.list.SongListViewModelBrain
import com.vgleadsheets.remaster.tags.list.TagListViewModelBrain
import com.vgleadsheets.remaster.tags.songs.TagValueSongsViewModelBrain
import com.vgleadsheets.remaster.tags.values.TagValuesViewModelBrain
import com.vgleadsheets.remaster.updates.UpdatesViewModelBrain
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.repository.history.UserContentGenerator
import com.vgleadsheets.repository.history.UserContentMigrator
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope

class FeatureDirectory(
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val randomRepository: RandomRepository,
    private val favoriteRepository: FavoriteRepository,
    private val tagRepository: TagRepository,
    private val dispatchers: VglsDispatchers,
    private val delayManager: DelayManager,
    private val appInfo: AppInfo,
    private val urlInfoProvider: UrlInfoProvider,
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val selectedPartManager: SelectedPartManager,
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    private val userContentGenerator: UserContentGenerator,
    private val userContentMigrator: UserContentMigrator,
    private val homeModuleProvider: HomeModuleProvider,
) : BrainProvider {
    @Suppress("LongMethod")
    override fun provideBrain(
        destination: Destination,
        coroutineScope: CoroutineScope
    ): ListViewModelBrain {
        val scheduler = ViewModelScheduler(
            coroutineScope,
            dispatchers,
            delayManager,
        )

        return when (destination) {
            Destination.HOME -> HomeViewModelBrain(
                stringProvider,
                hatchet,
                scheduler,
                homeModuleProvider,
                tagRepository,
                randomRepository,
            )

            Destination.BROWSE -> BrowseViewModelBrain(
                tagRepository,
                stringProvider,
                hatchet,
                scheduler,
            )

            Destination.PART_PICKER -> PartsListViewModelBrain(
                stringProvider,
                hatchet,
                scheduler,
                selectedPartManager,
            )

            Destination.GAME_DETAIL -> GameDetailViewModelBrain(
                songRepository,
                gameRepository,
                composerRepository,
                favoriteRepository,
                scheduler,
                stringProvider,
                hatchet,
            )

            Destination.GAMES_LIST -> GameListViewModelBrain(
                gameRepository,
                scheduler,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSER_DETAIL -> ComposerDetailViewModelBrain(
                songRepository,
                composerRepository,
                gameRepository,
                favoriteRepository,
                scheduler,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSERS_LIST -> ComposerListViewModelBrain(
                composerRepository,
                scheduler,
                stringProvider,
                hatchet,
            )

            Destination.SONG_DETAIL -> SongDetailViewModelBrain(
                songRepository,
                gameRepository,
                composerRepository,
                favoriteRepository,
                tagRepository,
                scheduler,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.SONGS_LIST -> SongListViewModelBrain(
                songRepository,
                scheduler,
                stringProvider,
                hatchet,
            )

            Destination.FAVORITES -> FavoritesViewModelBrain(
                favoriteRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.DIFFICULTY_LIST -> DifficultyListViewModelBrain(
                tagRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.DIFFICULTY_VALUES_LIST -> DifficultyValuesViewModelBrain(
                tagRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.TAGS_LIST -> TagListViewModelBrain(
                tagRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.TAGS_VALUES_LIST -> TagValuesViewModelBrain(
                tagRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.TAGS_VALUES_SONG_LIST -> TagValueSongsViewModelBrain(
                tagRepository,
                songRepository,
                scheduler,
                stringProvider,
                hatchet
            )

            Destination.MENU -> MenuViewModelBrain(
                generalSettingsManager,
                debugSettingsManager,
                userContentGenerator,
                userContentMigrator,
                appInfo,
                stringProvider,
                hatchet,
                scheduler,
            )

            Destination.UPDATES -> UpdatesViewModelBrain(
                stringProvider,
                hatchet,
                scheduler,
            )

            Destination.SEARCH,
            Destination.SONG_VIEWER,
            Destination.LICENSES -> throw IllegalArgumentException("Not a list view: $destination")
        }
    }
}
