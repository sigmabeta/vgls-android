package com.vgleadsheets.features.main.sheet.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.features.main.sheet.R
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterSongViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSongState,
    private val repository: Repository,
) : MvRxViewModel<BetterSongState>(initialState) {
    init {
        fetchSong()
        fetchTagValues()
    }

    fun onComposerClicked(composerId: Long, composerName: String) {
        if (composerId != ID_COMPOSER_MULTIPLE) {
            router.showSongListForComposer(composerId, composerName)
        }
    }

    fun onGameClicked(gameId: Long, gameName: String) {
        router.showSongListForGame(gameId, gameName)
    }

    fun onCtaClicked(clickedId: Int, song: Song) {
        when (clickedId) {
            R.drawable.ic_description_24dp -> showSongViewer(song)
            R.drawable.ic_play_circle_filled_24 -> showYoutubeSearch(song)
            else -> TODO("Unimplemented button")
        }
    }

    fun onTagValueClicked(
        id: Long
    ) {
        router.showSongListForTagValue(id)
    }

    private fun fetchSong() = withState {
        repository.getSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        song = it
                    )
                )
            }
    }

    private fun fetchTagValues() = withState {
        repository.getTagValuesForSong(it.songId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        tagValues = it
                    )
                )
            }
    }

    private fun showSongViewer(song: Song) {
        router.showSongViewer(
            song.id
        )
    }

    private fun showYoutubeSearch(song: Song) {
        val query = "${song.gameName} - ${song.name}"
        val youtubeUrl = getYoutubeSearchUrlForQuery(query)

        router.goToWebUrl(youtubeUrl)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterSongState,
        ): BetterSongViewModel
    }

    companion object : MvRxViewModelFactory<BetterSongViewModel, BetterSongState> {
        const val ID_COMPOSER_MULTIPLE = Long.MAX_VALUE

        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5

        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSongState
        ): BetterSongViewModel {
            val fragment: BetterSongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
