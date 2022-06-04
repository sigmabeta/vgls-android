package com.vgleadsheets.features.main.composers.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterComposerListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterComposerListState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterComposerListState>(initialState) {
    init {
        fetchComposers()
    }

    private fun fetchComposers() {
        repository.getComposers()
            .execute {
                copy(contentLoad = BetterComposerListContent(it))
            }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onComposerClicked(dataId: Long, name: String) {
        router.showSongListForComposer(dataId, name)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterComposerListState,
            router: FragmentRouter
        ): BetterComposerListViewModel
    }

    companion object : MvRxViewModelFactory<BetterComposerListViewModel, BetterComposerListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterComposerListState
        ): BetterComposerListViewModel {
            val fragment: BetterComposerListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
