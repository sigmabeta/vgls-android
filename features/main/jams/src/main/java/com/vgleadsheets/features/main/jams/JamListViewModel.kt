package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class JamListViewModel @AssistedInject constructor(
    @Assisted initialState: JamListState,
    private val repository: Repository
) : MvRxViewModel<JamListState>(initialState) {

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: JamListState): JamListViewModel
    }

    companion object : MvRxViewModelFactory<JamListViewModel, JamListState> {
        override fun create(viewModelContext: ViewModelContext, state: JamListState): JamListViewModel? {
            val fragment: JamListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.jamListViewModelFactory.create(state)
        }
    }
}
