package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class JamListViewModel @AssistedInject constructor(
    @Assisted initialState: JamListState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers
) : MavericksViewModel<JamListState>(initialState) {
    init {
        fetchJams()
    }

    fun refreshJams() {
        viewModelScope.launch(dispatchers.network) {
            repository.refreshJams()
        }
    }

    private fun fetchJams() {
        repository.getAllJams(false)
            .execute {
                copy(contentLoad = JamListContent(it))
            }
    }

    @AssistedFactory
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
