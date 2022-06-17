package com.vgleadsheets.features.main.sheet.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.sheet.R
import com.vgleadsheets.features.main.sheet.SheetDetailViewModel
import com.vgleadsheets.getYoutubeSearchUrlForQuery
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterSongViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSongState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterSongState>(initialState) {
    init {
        fetchSong()
        fetchTagValues()
    }

    fun onComposerClicked(composerId: Long, composerName: String) {
        if (composerId != SheetDetailViewModel.ID_COMPOSER_MULTIPLE) {
            router.showSongListForComposer(composerId, composerName)
        }
    }

    fun onGameClicked(gameId: Long, gameName: String) {
        router.showSongListForGame(gameId, gameName)
    }

    fun onCtaClicked(clickedId: Int, song: Song, selectedPart: Part) {
        when (clickedId) {
            R.drawable.ic_description_24dp -> showSongViewer(song, selectedPart)
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

    private fun showSongViewer(song: Song, selectedPart: Part) {
        router.showSongViewer(
            song.id,
            song.name,
            song.gameName,
            selectedPart.apiId
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
            router: FragmentRouter
        ): BetterSongViewModel
    }

    companion object : MvRxViewModelFactory<BetterSongViewModel, BetterSongState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSongState
        ): BetterSongViewModel {
            val fragment: BetterSongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
