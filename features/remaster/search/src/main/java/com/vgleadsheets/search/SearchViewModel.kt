package com.vgleadsheets.search

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.common.debug.ShowDebugProvider
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.DelayManager
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.SearchRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SearchViewModel @AssistedInject constructor(
    override val dispatchers: VglsDispatchers,
    override val delayManager: DelayManager,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    val stringProvider: StringProvider,
    private val searchRepository: SearchRepository,
    override val showDebugProvider: ShowDebugProvider,
    @Assisted("textUpdater") private val textUpdater: (String) -> Unit,
) : VglsViewModel<SearchState>(),
    ActionSink,
    EventSink {
    private var historyTimer: Job? = null

    private val internalResultItemsFlow = MutableStateFlow(initialState().toListItems(stringProvider))

    init {
        eventDispatcher.addEventSink(this)
        getSearchHistory()
        setupSearchInputObservation()
    }

    override fun initialState() = SearchState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")

            when (action) {
                is VglsAction.Resume -> onResume()
                is VglsAction.SearchClearClicked -> onSearchClearClicked()
                is VglsAction.SearchQueryEntered -> startSearch(action.query)
                is Action.SongClicked -> onSongClicked(action.id)
                is Action.GameClicked -> onGameClicked(action.id)
                is Action.ComposerClicked -> onComposerClicked(action.id)
                is Action.SearchHistoryEntryClicked -> onSearchHistoryEntryClicked(action.query)
                is Action.SearchHistoryEntryRemoveClicked -> searchRepository.removeFromSearchHistory(action.id)
                is VglsAction.AppBack -> navigateBack()
            }
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("${this@SearchViewModel.javaClass.simpleName} - Handling event: $event")
        }
    }

    override fun updateState(updater: (SearchState) -> SearchState) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            val oldState = internalUiState.value
            val newState = updater(oldState)

            internalUiState.value = newState
            internalResultItemsFlow.value = newState.toListItems(stringProvider)
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

    private fun onSearchHistoryEntryClicked(query: String) {
        updateTextField(query)
        startSearch(query)
    }

    private fun onSearchClearClicked() {
        updateTextField("")
        clearQuery()
        clearResults()
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
        updateSearchHistory(LCE.Loading(LOAD_OPERATION_HISTORY))
        searchRepository.getRecentSearches()
            .onEach { history -> updateSearchHistory(LCE.Content(history)) }
            .catch { updateSearchHistory(LCE.Error(LOAD_OPERATION_HISTORY, it)) }
            .runInBackground()
    }

    private fun updateTextField(newText: String) {
        textUpdater(newText)
    }

    private fun setupSearchInputObservation() {
        internalUiState
            .map { it.searchQuery.trim() }
            .distinctUntilChanged()
            .onEach { showLoadingResultsMaybe() }
            .launchIn(viewModelScope)

        latestQuery()
            .onEach { query ->
                if (query.length >= MINIMUM_LENGTH_QUERY) {
                    startHistoryTimer(query)
                } else {
                    clearResults()
                }
            }
            .launchIn(viewModelScope)

        observeSearchInput(
            searchOperation = {
                searchRepository.searchGamesCombined(it)
            },
            onSearchError = { updateGames(LCE.Error(LOAD_OPERATION_GAMES, it)) },
            onSearchSuccess = { results -> updateGames(LCE.Content(results)) },
        )

        observeSearchInput(
            searchOperation = {
                searchRepository.searchSongsCombined(it)
            },
            onSearchError = { updateSongs(LCE.Error(LOAD_OPERATION_SONGS, it)) },
            onSearchSuccess = { results -> updateSongs(LCE.Content(results)) },
        )

        observeSearchInput(
            searchOperation = {
                searchRepository.searchComposersCombined(it)
            },
            onSearchError = { updateComposers(LCE.Error(LOAD_OPERATION_COMPOSER, it)) },
            onSearchSuccess = { results -> updateComposers(LCE.Content(results)) },
        )
    }

    private fun showLoadingResultsMaybe() {
        val state = internalUiState.value

        if (!state.songResults.containsAnyResults()) {
            updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
        }

        if (!state.gameResults.containsAnyResults()) {
            updateGames(LCE.Loading(LOAD_OPERATION_GAMES))
        }

        if (!state.composerResults.containsAnyResults()) {
            updateComposers(LCE.Loading(LOAD_OPERATION_COMPOSER))
        }
    }

    private fun clearQuery() {
        hatchet.v("Clearing query")
        updateState {
            it.copy(
                searchQuery = "",
            )
        }
    }

    private fun clearResults() {
        hatchet.v("Clearing results")
        updateState {
            it.copy(
                songResults = LCE.Uninitialized,
                gameResults = LCE.Uninitialized,
                composerResults = LCE.Uninitialized,
            )
        }
    }

    private fun startHistoryTimer(query: String) {
        historyTimer?.cancel()
        historyTimer = viewModelScope.launch(scheduler.dispatchers.disk) {
            delay(DURATION_HISTORY_RECORD)

            val state = internalUiState.value
            val history = state.searchHistory
            if (history !is LCE.Content) {
                return@launch
            }

            val existingQueriesInHistory = history.data.map { it.query }

            if (existingQueriesInHistory.contains(query)) {
                hatchet.i("Not adding query $query to history because it's already there.")
                return@launch
            }

            val anySearchResultsFound = state.composerResults.containsAnyResults() ||
                state.gameResults.containsAnyResults() ||
                state.songResults.containsAnyResults()

            if (!anySearchResultsFound) {
                hatchet.i("Not adding query $query to history because it found no results.")
                return@launch
            }

            hatchet.d("Adding query $query to history.")
            searchRepository.addToSearchHistory(query)
            historyTimer = null
        }
    }

    private fun stopHistoryTimer() {
        if (historyTimer != null) {
            historyTimer?.cancel()
            historyTimer = null
        }
    }

    private fun <ModelType> observeSearchInput(
        searchOperation: suspend (String) -> Flow<List<ModelType>>,
        onSearchError: suspend FlowCollector<List<ModelType>>.(cause: Throwable) -> Unit,
        onSearchSuccess: suspend (List<ModelType>) -> Unit,
    ) {
        latestQuery()
            .filter { it.length >= MINIMUM_LENGTH_QUERY }
            .flatMapLatest(searchOperation)
            .onEach(onSearchSuccess)
            .catch(onSearchError)
            .runInBackground()
    }

    private fun latestQuery() = internalUiState
        .map { it.searchQuery.trim() }
        .distinctUntilChanged()
        .debounce(DEBOUNCE_THRESHOLD)

    private fun updateSearchHistory(searchHistory: LCE<List<SearchHistoryEntry>>) {
        updateState {
            it.copy(
                searchHistory = searchHistory
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            it.copy(
                songResults = songs
            )
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            it.copy(
                composerResults = composers
            )
        }
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            it.copy(
                gameResults = games
            )
        }
    }

    private fun navigateBack() {
        eventDispatcher.sendEvent(
            VglsEvent.NavigateBack(
                "Search"
            )
        )
    }

    private fun <ItemType> LCE<List<ItemType>>.containsAnyResults(): Boolean {
        if (this !is LCE.Content) {
            return false
        }

        val data = this.data
        return data.isNotEmpty()
    }

    companion object {
        private const val DURATION_HISTORY_RECORD = 3_000L

        internal const val DEBOUNCE_THRESHOLD = 300L
        internal const val MINIMUM_LENGTH_QUERY = 3

        internal const val LOAD_OPERATION_HISTORY = "search.history"
        internal const val LOAD_OPERATION_COMPOSER = "search.composers"
        internal const val LOAD_OPERATION_SONGS = "search.songs"
        internal const val LOAD_OPERATION_GAMES = "search.games"
    }
}
