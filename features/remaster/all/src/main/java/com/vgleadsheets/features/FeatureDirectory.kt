package com.vgleadsheets.features

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.BrainProvider
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.remaster.browse.BrowseViewModelBrain
import com.vgleadsheets.remaster.composers.detail.ComposerDetailViewModelBrain
import com.vgleadsheets.remaster.composers.list.ComposerListViewModelBrain
import com.vgleadsheets.remaster.games.detail.GameDetailViewModelBrain
import com.vgleadsheets.remaster.games.list.GameListViewModelBrain
import com.vgleadsheets.remaster.home.HomeViewModelBrain
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope

class FeatureDirectory(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val urlInfoProvider: UrlInfoProvider,
): BrainProvider {
    override fun provideBrain(
        destination: Destination,
        coroutineScope: CoroutineScope
    ): ListViewModelBrain {
        return when (destination) {
            Destination.HOME -> HomeViewModelBrain()
            Destination.BROWSE -> BrowseViewModelBrain()
            Destination.GAME_DETAIL -> GameDetailViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                urlInfoProvider
            )
            Destination.GAMES_LIST -> GameListViewModelBrain(
                repository,
                dispatchers,
                coroutineScope
            )
            Destination.COMPOSER_DETAIL -> ComposerDetailViewModelBrain(
                repository,
                dispatchers,
                coroutineScope,
                urlInfoProvider
            )
            Destination.COMPOSERS_LIST -> ComposerListViewModelBrain(
                repository,
                dispatchers,
                coroutineScope
            )
            Destination.SONG_VIEWER -> TODO()
            Destination.SONG_DETAIL -> TODO()
            Destination.SONGS_LIST -> TODO()
            Destination.TAGS_LIST -> TODO()
            Destination.FAVORITES -> TODO()
        }
    }
}
