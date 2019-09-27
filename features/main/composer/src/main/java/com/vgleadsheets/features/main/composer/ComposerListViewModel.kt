package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class ComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerListState,
    private val repository: Repository
) : MvRxViewModel<ComposerListState>(initialState) {
    init {
        fetchComposers()
    }

    private fun fetchComposers() {
        repository.getComposers()
            .execute { data ->
                copy(composers = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: ComposerListState): ComposerListViewModel
    }

    companion object : MvRxViewModelFactory<ComposerListViewModel, ComposerListState> {
        override fun create(viewModelContext: ViewModelContext, state: ComposerListState): ComposerListViewModel? {
            val fragment: ComposerListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.composerListViewModelFactory.create(state)
        }
    }
}
