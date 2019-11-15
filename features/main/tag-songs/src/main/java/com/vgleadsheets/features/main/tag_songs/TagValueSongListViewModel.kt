package com.vgleadsheets.features.main.tag_songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class TagValueSongListViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueSongListState,
    private val repository: Repository
) : MvRxViewModel<TagValueSongListState>(initialState){
    init {
        fetchTagValue()
        fetchSongs()
    }

    private fun fetchTagValue() = withState { state ->
        repository.getTagValue(state.tagValueId)
            .execute { data ->
                copy(tagValue = data)
            }
    }

    private fun fetchSongs() = withState { state ->
        repository.getSongsForTagValue(state.tagValueId)
            .execute { data ->
                copy(songs = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagValueSongListState): TagValueSongListViewModel
    }

    companion object : MvRxViewModelFactory<TagValueSongListViewModel, TagValueSongListState> {
        override fun create(viewModelContext: ViewModelContext, state: TagValueSongListState): TagValueSongListViewModel? {
            val fragment: TagValueSongListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagValueViewModelFactory.create(state)
        }
    }
}
