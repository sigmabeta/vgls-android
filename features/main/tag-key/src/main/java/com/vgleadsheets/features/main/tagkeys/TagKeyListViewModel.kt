package com.vgleadsheets.features.main.tagkeys

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TagKeyListViewModel @AssistedInject constructor(
    @Assisted initialState: TagKeyListState,
    private val repository: VglsRepository,
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

    @AssistedFactory
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
