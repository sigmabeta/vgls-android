package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class ComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerListState,
    private val repository: Repository,
) : MavericksViewModel<ComposerListState>(initialState) {
    init {
        fetchComposers()
    }

    private fun fetchComposers() {
        repository.getComposers()
            .execute {
                copy(contentLoad = ComposerListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: ComposerListState
        ): ComposerListViewModel
    }

    companion object : MavericksViewModelFactory<ComposerListViewModel, ComposerListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ComposerListState
        ): ComposerListViewModel {
            val fragment: ComposerListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
