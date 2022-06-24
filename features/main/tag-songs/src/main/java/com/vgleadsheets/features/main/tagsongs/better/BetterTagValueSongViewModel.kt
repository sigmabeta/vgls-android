package com.vgleadsheets.features.main.tagsongs.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterTagValueSongViewModel @AssistedInject constructor(
    @Assisted initialState: BetterTagValueSongState,
    private val repository: Repository,
) : MvRxViewModel<BetterTagValueSongState>(initialState) {
    init {
        fetchTagValue()
        fetchSongs()
    }

    private fun fetchTagValue() = withState {
        repository.getTagValue(it.tagValueId)
            .execute { tagValue ->
                copy(
                    contentLoad = contentLoad.copy(
                        tagValue = tagValue
                    )
                )
            }
    }

    private fun fetchSongs() = withState {
        repository.getSongsForTagValue(it.tagValueId)
            .execute { songs ->
                copy(
                    contentLoad = contentLoad.copy(
                        songs = songs
                    )
                )
            }
    }

    fun onSongClicked(
        id: Long
    ) {
        router.showSongViewer(
            id
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterTagValueSongState,
        ): BetterTagValueSongViewModel
    }

    companion object : MvRxViewModelFactory<BetterTagValueSongViewModel, BetterTagValueSongState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterTagValueSongState
        ): BetterTagValueSongViewModel {
            val fragment: BetterTagValueSongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
