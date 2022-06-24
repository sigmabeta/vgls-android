package com.vgleadsheets.features.main.search.better

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

class BetterSearchViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSearchState,
    private val repository: Repository,
) : MvRxViewModel<BetterSearchState>(initialState) {

    private val searchOperations = CompositeDisposable()

    fun showStickerBr(query: String) = setState {
        copy(
            showStickerBr = true,
            query = query
        )
    }

    fun startQuery(searchQuery: String) {
        withState { state ->
            if (state.query != searchQuery) {
                searchOperations.clear()

                setState {
                    copy(
                        query = searchQuery,
                        showStickerBr = false
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

    fun onQueryClear() {
        setState {
            copy(
                null,
                false,
                contentLoad = contentLoad.copy(
                    Uninitialized,
                    Uninitialized,
                    Uninitialized
                )
            )
        }
    }

    fun onSongClicked(
        id: Long
    ) {
        router.showSongViewer(
            id
        )
    }

    fun onGameClicked(id: Long, name: String) {
        router.showSongListForGame(id, name)
    }

    fun onComposerClicked(id: Long, name: String) {
        router.showSongListForComposer(id, name)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterSearchState,
        ): BetterSearchViewModel
    }

    companion object : MvRxViewModelFactory<BetterSearchViewModel, BetterSearchState> {
        const val RESULT_DEBOUNCE_THRESHOLD = 250L

        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSearchState
        ): BetterSearchViewModel {
            val fragment: BetterSearchFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
