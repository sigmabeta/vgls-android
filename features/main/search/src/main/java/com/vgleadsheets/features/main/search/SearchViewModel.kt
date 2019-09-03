package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: Repository
) : MvRxViewModel<SearchState>(initialState) {
    fun startQuery(searchQuery: String) {
        withState {
            if (it.query != searchQuery) {
                setState { copy(query = searchQuery) }
                repository.search(searchQuery)
                    .execute {
                        copy(results = it)
                    }
            }
        }
    }

    fun onItemClick(position: Int) {
        withState {
            if (it.results is Success) {
                val results = it.results()
                val clickedResult = results?.get(position)
                if (clickedResult != null) {
                    setState { copy(clickedId = clickedResult.id) }
                }
            }
        }
    }

    fun onSongLaunch() {
        setState { copy(clickedId = null) }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SearchState): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        override fun create(viewModelContext: ViewModelContext, state: SearchState): SearchViewModel? {
            val fragment: SearchFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.searchViewModelFactory.create(state)
        }
    }
}
