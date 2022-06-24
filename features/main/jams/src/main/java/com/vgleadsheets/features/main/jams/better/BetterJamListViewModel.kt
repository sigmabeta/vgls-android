package com.vgleadsheets.features.main.jams.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterJamListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterJamListState,
    private val repository: Repository,
) : MvRxViewModel<BetterJamListState>(initialState) {
    init {
        fetchJams()
    }

    private fun fetchJams() {
        repository.getJams()
            .execute {
                copy(contentLoad = BetterJamListContent(it))
            }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onJamClicked(dataId: Long, name: String) {
        router.showJamDetailViewer(dataId)
    }

    fun onFindJamClicked() {
        router.showFindJamDialog()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterJamListState,
        ): BetterJamListViewModel
    }

    companion object : MvRxViewModelFactory<BetterJamListViewModel, BetterJamListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterJamListState
        ): BetterJamListViewModel {
            val fragment: BetterJamListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
