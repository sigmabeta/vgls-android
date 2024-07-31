package com.vgleadsheets.features

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.remaster.browse.BrowseViewModelBrain
import com.vgleadsheets.remaster.composers.detail.ComposerDetailViewModelBrain
import com.vgleadsheets.remaster.composers.list.ComposerListViewModelBrain
import com.vgleadsheets.remaster.games.detail.GameDetailViewModelBrain
import com.vgleadsheets.remaster.games.list.GameListViewModelBrain
import com.vgleadsheets.remaster.home.HomeModuleProvider
import com.vgleadsheets.remaster.home.HomeViewModelBrain
import com.vgleadsheets.remaster.parts.PartsListViewModelBrain
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain
import com.vgleadsheets.remaster.songs.list.SongListViewModelBrain
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.RandomRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope

class FeatureDirectory(
    private val repository: VglsRepository,
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val randomRepository: RandomRepository,
    private val favoriteRepository: FavoriteRepository,
    private val dispatchers: VglsDispatchers,
    private val urlInfoProvider: UrlInfoProvider,
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val selectedPartManager: SelectedPartManager,
    private val homeModuleProvider: HomeModuleProvider,
) : BrainProvider {
    @Suppress("LongMethod")
    override fun provideBrain(
        destination: Destination,
        coroutineScope: CoroutineScope
    ): ListViewModelBrain {
        return when (destination) {
            Destination.HOME -> HomeViewModelBrain(
                stringProvider,
                hatchet,
                dispatchers,
                coroutineScope,
                homeModuleProvider,
                randomRepository,
            )

            Destination.BROWSE -> BrowseViewModelBrain(
                stringProvider,
                hatchet,
                dispatchers,
                coroutineScope,
            )

            Destination.PART_PICKER -> PartsListViewModelBrain(
                stringProvider,
                hatchet,
                dispatchers,
                coroutineScope,
                selectedPartManager,
            )

            Destination.GAME_DETAIL -> GameDetailViewModelBrain(
                repository,
                songRepository,
                gameRepository,
                composerRepository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.GAMES_LIST -> GameListViewModelBrain(
                gameRepository,
                dispatchers,
                coroutineScope,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSER_DETAIL -> ComposerDetailViewModelBrain(
                songRepository,
                composerRepository,
                gameRepository,
                favoriteRepository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSERS_LIST -> ComposerListViewModelBrain(
                composerRepository,
                dispatchers,
                coroutineScope,
                stringProvider,
                hatchet,
            )

            Destination.SONG_VIEWER -> TODO()
            Destination.SONG_DETAIL -> SongDetailViewModelBrain(
                repository,
                songRepository,
                gameRepository,
                composerRepository,
                favoriteRepository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.SONGS_LIST -> SongListViewModelBrain(
                songRepository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.FAVORITES -> TODO()
            Destination.TAGS_LIST -> TODO()
            Destination.TAGS_VALUES_LIST -> TODO()
            Destination.TAGS_VALUES_SONG_LIST -> TODO()
            Destination.MENU -> throw IllegalArgumentException("Not a list view: $destination")
            Destination.SEARCH -> throw IllegalArgumentException("Not a list view: $destination")
        }
    }
}
