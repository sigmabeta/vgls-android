package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: Repository,
) : MavericksViewModel<SearchState>(initialState) {
    init {
        withState {
            if (!it.initialQuery.isNullOrEmpty()) {
                startQuery(it.initialQuery)
            }
        }
    }

    private var searchOperations = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    @OptIn(FlowPreview::class)
    fun startQuery(searchQuery: String) {
        withState { state ->
            if (state.query != searchQuery) {
                searchOperations.cancel()

                setState {
                    copy(
                        query = searchQuery
                    )
                }

                repository.searchGamesCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD)
                    .execute { newGames ->
                        copy(
                            contentLoad = contentLoad.copy(
                                games = newGames
                            )
                        )
                    }

                repository.searchSongs(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD)
                    .execute { newSongs ->
                        copy(
                            contentLoad = contentLoad.copy(
                                songs = newSongs
                            )
                        )
                    }

                repository.searchComposersCombined(searchQuery)
                    .debounce(RESULT_DEBOUNCE_THRESHOLD)
                    .execute { newComposers ->
                        copy(
                            contentLoad = contentLoad.copy(
                                composers = newComposers
                            )
                        )
                    }

                // TODO Figure this out
                // searchOperations.addAll(gameSearch, songSearch, composerSearch)
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

    companion object : MavericksViewModelFactory<SearchViewModel, SearchState> {
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
