package com.vgleadsheets.features.main.tagsongs.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.TrackingScreen

class BetterTagValueSongViewModel @AssistedInject constructor(
    @Assisted initialState: BetterTagValueSongState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterTagValueSongState>(initialState) {
    init {
        fetchTagValue()
        fetchSongs()
    }

    private fun fetchTagValue() = withState {
        repository.getTagValue(it.tagValueId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        tagValue = it
                    )
                )
            }
    }

    private fun fetchSongs() = withState {
        repository.getSongsForTagValue(it.tagValueId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songs = it
                    )
                )
            }
    }

    fun onSongClicked(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String
    ) {
        router.showSongViewer(
            id,
            songName,
            gameName,
            transposition,
            TrackingScreen.DETAIL_GAME,
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterTagValueSongState,
            router: FragmentRouter
        ): BetterTagValueSongViewModel
    }

    companion object : MvRxViewModelFactory<BetterTagValueSongViewModel, BetterTagValueSongState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterTagValueSongState
        ): BetterTagValueSongViewModel {
            val fragment: BetterTagValueSongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
