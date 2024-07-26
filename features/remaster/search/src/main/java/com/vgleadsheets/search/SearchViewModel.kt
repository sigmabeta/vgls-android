package com.vgleadsheets.search

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.bottombar.SearchState
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.SearchRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    val stringProvider: StringProvider,
    private val searchRepository: SearchRepository,
) : VglsViewModel<SearchState>(),
    ActionSink,
    EventSink {
    private var historyTimer: Job? = null

    private val internalResultItemsFlow = MutableStateFlow(initialState().resultItems(stringProvider))
    val resultItemsFlow = internalResultItemsFlow
        .asStateFlow()

    init {
        eventDispatcher.addEventSink(this)
        getSearchHistory()
        setupSearchInputObservation()
    }

    override fun initialState() = SearchState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")

            when (action) {
                is VglsAction.Resume -> onResume()
                is VglsAction.SearchClearClicked -> startSearch("")
                is VglsAction.SearchQueryEntered -> startSearch(action.query)
                is Action.SongClicked -> onSongClicked(action.id)
                is Action.GameClicked -> onGameClicked(action.id)
                is Action.ComposerClicked -> onComposerClicked(action.id)
                is Action.SearchHistoryEntryClicked -> startSearch(action.query)
                is Action.SearchHistoryEntryRemoveClicked -> searchRepository.removeFromSearchHistory(action.id)
            }
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this@SearchViewModel.javaClass.simpleName} - Handling event: $event")
        }
    }

    override fun updateState(updater: (SearchState) -> SearchState) {
        viewModelScope.launch(dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            internalUiState.value = newState
            internalResultItemsFlow.value = newState.resultItems(stringProvider)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopHistoryTimer()
    }

    private fun onResume() {
        emitEvent(VglsEvent.ShowUiChrome)
        emitEvent(VglsEvent.HideTopBar)
    }

    private fun startSearch(query: String) {
        updateState {
            it.copy(
                searchQuery = query
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.SEARCH.template()
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.SEARCH.template()
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.SEARCH.template()
            )
        )
    }

    private fun getSearchHistory() {
        searchRepository.getRecentSearches()
            .onEach { history ->
                updateState {
                    it.copy(
                        searchHistory = history
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun setupSearchInputObservation() {
        internalUiState
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300L)
            .onEach { query ->
                if (query.length >= MINIMUM_LENGTH_QUERY) {
                    startHistoryTimer(query)
                } else {
                    clearSearch()
                }
            }
            .launchIn(viewModelScope)

        observeSearchInput(
            searchOperation = { searchRepository.searchGamesCombined(it) },
            onSearchSuccess = { results ->
                updateState {
                    it.copy(gameResults = results)
                }
            }
        )

        observeSearchInput(
            searchOperation = { searchRepository.searchSongsCombined(it) },
            onSearchSuccess = { results ->
                updateState {
                    it.copy(songResults = results)
                }
            }
        )

        observeSearchInput(
            searchOperation = { searchRepository.searchComposersCombined(it) },
            onSearchSuccess = { results ->
                updateState {
                    it.copy(composerResults = results)
                }
            }
        )
    }

    private fun clearSearch() {
        hatchet.v("Clearing search")
        updateState {
            it.copy(
                searchQuery = "",
                songResults = emptyList(),
                gameResults = emptyList(),
                composerResults = emptyList(),
            )
        }
    }

    private fun startHistoryTimer(query: String) {
        historyTimer?.cancel()
        historyTimer = viewModelScope.launch(dispatchers.disk) {
            delay(DURATION_HISTORY_RECORD)

            searchRepository.addToSearchHistory(query)
            hatchet.d("This search has officially been recorded.")
            historyTimer = null
        }
    }

    private fun stopHistoryTimer() {
        if (historyTimer != null) {
            hatchet.i("This search was not officially recorded.")
            historyTimer?.cancel()
            historyTimer = null
        }
    }

    private fun <ModelType> observeSearchInput(
        searchOperation: suspend (String) -> Flow<List<ModelType>>,
        onSearchSuccess: suspend (List<ModelType>) -> Unit,
    ) {
        internalUiState
            .map { it.searchQuery }
            .debounce(DEBOUNCE_THRESHOLD)
            .filter { it.length >= MINIMUM_LENGTH_QUERY }
            .flatMapLatest(searchOperation)
            .catch { hatchet.e("Error searching: ${it.message}") }
            .onEach(onSearchSuccess)
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    companion object {
        private const val DURATION_HISTORY_RECORD = 1_000L

        internal const val DEBOUNCE_THRESHOLD = 300L
        private const val MINIMUM_LENGTH_QUERY = 3
    }
}
