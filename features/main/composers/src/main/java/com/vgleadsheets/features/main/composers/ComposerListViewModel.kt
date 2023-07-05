package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerListState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    ) : MavericksViewModel<ComposerListState>(initialState) {
    init {
        fetchComposers()
    }

    private fun fetchComposers() {
        repository.getAllComposers()
            .execute {
                copy(composers = it)
            }
    }

    @AssistedFactory
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
