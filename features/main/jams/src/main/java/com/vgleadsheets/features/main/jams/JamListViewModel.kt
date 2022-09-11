package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class JamListViewModel @AssistedInject constructor(
    @Assisted initialState: JamListState,
    private val repository: Repository,
) : MavericksViewModel<JamListState>(initialState) {
    init {
        fetchJams()
    }

    private fun fetchJams() {
        repository.getJams()
            .execute {
                copy(contentLoad = JamListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: JamListState,
        ): JamListViewModel
    }

    companion object : MavericksViewModelFactory<JamListViewModel, JamListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: JamListState
        ): JamListViewModel {
            val fragment: JamListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
