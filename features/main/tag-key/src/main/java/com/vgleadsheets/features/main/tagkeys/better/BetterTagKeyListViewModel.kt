package com.vgleadsheets.features.main.tagkeys.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterTagKeyListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterTagKeyListState,
    private val repository: Repository,
) : MvRxViewModel<BetterTagKeyListState>(initialState) {
    init {
        fetchTagKeys()
    }

    private fun fetchTagKeys() {
        repository.getAllTagKeys()
            .execute {
                copy(contentLoad = BetterTagKeyListContent(it))
            }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onTagKeyClicked(dataId: Long, name: String) {
        router.showValueListForTagKey(dataId)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterTagKeyListState,
        ): BetterTagKeyListViewModel
    }

    companion object : MvRxViewModelFactory<BetterTagKeyListViewModel, BetterTagKeyListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterTagKeyListState
        ): BetterTagKeyListViewModel {
            val fragment: BetterTagKeyListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
