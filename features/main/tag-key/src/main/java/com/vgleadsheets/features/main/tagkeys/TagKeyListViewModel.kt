package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class TagKeyListViewModel @AssistedInject constructor(
    @Assisted initialState: TagKeyListState,
    private val repository: Repository,
) : MavericksViewModel<TagKeyListState>(initialState) {
    init {
        fetchTagKeys()
    }

    private fun fetchTagKeys() {
        repository.getAllTagKeys()
            .execute {
                copy(contentLoad = TagKeyListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: TagKeyListState,
        ): TagKeyListViewModel
    }

    companion object : MavericksViewModelFactory<TagKeyListViewModel, TagKeyListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagKeyListState
        ): TagKeyListViewModel {
            val fragment: TagKeyListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
