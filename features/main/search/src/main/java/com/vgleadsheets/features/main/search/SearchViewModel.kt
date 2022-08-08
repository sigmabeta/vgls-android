package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: Repository,
) : MvRxViewModel<SearchState>(initialState) {
    init {
        withState {
            if (!it.initialQuery.isNullOrEmpty()) {
                startQuery(it.initialQuery)
            }
        }
    }

    private val searchOperations = CompositeDisposable()

    fun startQuery(searchQuery: String) {
        withState { state ->
            if (state.query != searchQuery) {
                searchOperations.clear()

                setState {
                    copy(
                        query = searchQuery
                    )
                }

                val gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newGames ->
                        copy(
                            contentLoad = contentLoad.copy(
                                games = newGames
                            )
                        )
                    }

                val songSearch = repository.searchSongs(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newSongs ->
                        copy(
                            contentLoad = contentLoad.copy(
                                songs = newSongs
                            )
                        )
                    }

                val composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD, TimeUnit.MILLISECONDS)
                    .execute { newComposers ->
                        copy(
                            contentLoad = contentLoad.copy(
                                composers = newComposers
                            )
                        )
                    }

                searchOperations.addAll(gameSearch, songSearch, composerSearch)
            }
        }
    }

    fun clearQuery() {
        setState {
            copy(
                null,
                contentLoad = contentLoad.copy(
                    Uninitialized,
                    Uninitialized,
                    Uninitialized
                )
            )
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: SearchState,
        ): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchState> {
        const val RESULT_DEBOUNCE_THRESHOLD = 250L

        override fun create(
            viewModelContext: ViewModelContext,
            state: SearchState
        ): SearchViewModel {
            val fragment: SearchFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
