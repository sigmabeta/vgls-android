package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.debounce

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchState,
    private val repository: VglsRepository,
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

                repository.searchSongsCombined(searchQuery)
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

    @AssistedFactory
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
