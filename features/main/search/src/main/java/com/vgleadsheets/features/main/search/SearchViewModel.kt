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
        withState { state ->
            if (state.query != searchQuery) {
                setState { copy(query = searchQuery) }

                repository.searchGames(searchQuery)
                    .execute {
                        copy(games = it)
                    }

                repository.searchSongs(searchQuery)
                    .execute {
                        copy(songs = it)
                    }

                repository.searchComposers(searchQuery)
                    .execute {
                        copy(composers = it)
                    }
            }
        }
    }

    fun clearResults() = setState {
        copy(
            games = Success(arrayListOf()),
            composers = Success(arrayListOf()),
            songs = Success(arrayListOf())
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SearchState): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchState
        ): SearchViewModel? {
            val fragment: SearchFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.searchViewModelFactory.create(state)
        }
    }
}
