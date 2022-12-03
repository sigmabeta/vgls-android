package com.vgleadsheets.features.main.tagsongs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TagValueSongViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueSongState,
    private val repository: VglsRepository,
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

    @AssistedFactory
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
