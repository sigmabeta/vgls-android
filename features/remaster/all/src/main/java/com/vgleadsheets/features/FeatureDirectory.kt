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
import com.vgleadsheets.remaster.home.HomeViewModelBrain
import com.vgleadsheets.remaster.parts.PartsListViewModelBrain
import com.vgleadsheets.remaster.songs.detail.SongDetailViewModelBrain
import com.vgleadsheets.remaster.songs.list.SongListViewModelBrain
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.settings.part.SelectedPartManager
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope

class FeatureDirectory(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val urlInfoProvider: UrlInfoProvider,
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val selectedPartManager: SelectedPartManager,
) : BrainProvider {
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
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.GAMES_LIST -> GameListViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSER_DETAIL -> ComposerDetailViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.COMPOSERS_LIST -> ComposerListViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                stringProvider,
                hatchet,
            )

            Destination.SONG_VIEWER -> TODO()
            Destination.SONG_DETAIL -> SongDetailViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                urlInfoProvider,
                stringProvider,
                hatchet,
            )

            Destination.SONGS_LIST -> SongListViewModelBrain(
                repository,
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
        }
    }
}
