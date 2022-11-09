package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class TagValueSongViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueSongState,
    private val repository: Repository,
) : MavericksViewModel<TagValueSongState>(initialState) {
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

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: TagValueSongState,
        ): TagValueSongViewModel
    }

    companion object : MavericksViewModelFactory<TagValueSongViewModel, TagValueSongState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagValueSongState
        ): TagValueSongViewModel {
            val fragment: TagValueSongFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
