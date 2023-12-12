package com.vgleadsheets.features.main.hud

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HudViewModel
@AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
) : MavericksViewModel<HudState>(initialState) {
    private var gameSearch: Job? = null
    private var composerSearch: Job? = null
    private var songSearch: Job? = null

    private val searchQueryQueue = MutableSharedFlow<String>()

    init {
        subscribeToSearchQueryQueue()
    }

    fun clearSearchQuery() = setState {
        copy(
            searchQuery = null,
            searchResults = searchResults.copy(
                songs = Uninitialized,
                composers = Uninitialized,
                games = Uninitialized
            )
        )
    }

    fun queueSearchQuery(query: String) = viewModelScope.launch(dispatchers.computation) {
        searchQueryQueue.emit(query)
    }

    private fun startSearchQuery(searchQuery: String) {
        withState { state ->
            if (state.searchQuery != searchQuery) {
                cancelSearch()

                setState {
                    copy(
                        searchQuery = searchQuery,
                        searchResults = searchResults.copy(
                            searching = true
                        )
                    )
                }

                gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newGames ->
                        if (newGames is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                games = newGames,
                                searching = false
                            )
                        )
                    }

                songSearch = repository.searchSongsCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newSongs ->
                        if (newSongs is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                songs = newSongs,
                                searching = false
                            )
                        )
                    }

                composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newComposers ->
                        if (newComposers is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                composers = newComposers,
                                searching = false
                            )
                        )
                    }
            }
        }
    }

    private fun subscribeToSearchQueryQueue() {
        searchQueryQueue
            .sample(THRESHOLD_SEARCH_INPUTS)
            .onEach { startSearchQuery(it) }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)
    }

    private fun cancelSearch() {
        gameSearch?.cancel()
        composerSearch?.cancel()
        songSearch?.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: HudState,
        ): HudViewModel
    }

    interface HudViewModelFactoryProvider {
        var hudViewModelFactory: Factory
    }

    companion object : MavericksViewModelFactory<HudViewModel, HudState> {

        const val THRESHOLD_SEARCH_INPUTS = 500L
        const val THRESHOLD_RESULT_DEBOUNCE = 250L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as HudViewModelFactoryProvider
            return provider.hudViewModelFactory.create(state)
        }
    }
}
