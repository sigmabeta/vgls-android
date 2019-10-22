package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: Repository
) : MvRxViewModel<SearchState>(initialState) {
    private val searchOperations = CompositeDisposable()

    fun startQuery(searchQuery: String) {
        withState { state ->
            if (state.query != searchQuery) {
                setState { copy(query = searchQuery) }
                searchOperations.clear()

                val gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute {
                        copy(games = it)
                    }

                val songSearch = repository.searchSongs(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute {
                        copy(songs = it)
                    }

                val composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute {
                        copy(composers = it)
                    }

                searchOperations.addAll(gameSearch, songSearch, composerSearch)
            }
        }
    }

    fun onGbGameNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForGame(vglsId, name)
    }

    fun onGbComposerNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForComposer(vglsId, name)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SearchState): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        const val RESULT_DEBOUNCE_THRESHOLD = 500L

        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchState
        ): SearchViewModel? {
            val fragment: SearchFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.searchViewModelFactory.create(state)
        }
    }
}
