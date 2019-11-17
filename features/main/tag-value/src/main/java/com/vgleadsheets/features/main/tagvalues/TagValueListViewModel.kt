package com.vgleadsheets.features.main.tagvalues

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class TagValueListViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueListState,
    private val repository: Repository
) : MvRxViewModel<TagValueListState>(initialState) {
    init {
        fetchTagKey()
        fetchSongs()
    }

    private fun fetchTagKey() = withState { state ->
        repository.getTagKey(state.tagKeyId)
            .execute { data ->
                copy(tagKey = data)
            }
    }

    private fun fetchSongs() = withState { state ->
        repository.getTagValuesForTagKey(state.tagKeyId)
            .execute { data ->
                copy(tagValues = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagValueListState): TagValueListViewModel
    }

    companion object : MvRxViewModelFactory<TagValueListViewModel, TagValueListState> {
        override fun create(viewModelContext: ViewModelContext, state: TagValueListState): TagValueListViewModel? {
            val fragment: TagValueListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagValueViewModelFactory.create(state)
        }
    }
}
