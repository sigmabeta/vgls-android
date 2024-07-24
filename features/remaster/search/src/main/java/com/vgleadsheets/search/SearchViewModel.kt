package com.vgleadsheets.bottombar

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.EventSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.search.Action
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    private val stringProvider: StringProvider,
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
) : VglsViewModel<SearchState>(),
    ActionSink,
    EventSink {
    private val internalQueryFlow = MutableStateFlow("")

    private val internalResultItemsFlow = MutableStateFlow(initialState().resultItems(stringProvider))
    val resultItemsFlow = internalResultItemsFlow
        .asStateFlow()

    init {
        eventDispatcher.addEventSink(this)
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

    private fun onResume() {
        emitEvent(VglsEvent.ShowUiChrome)
        emitEvent(VglsEvent.HideTopBar)
    }

    private fun startSearch(query: String) {
        internalQueryFlow.value = query
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

    private fun setupSearchInputObservation() {
        internalQueryFlow
            .filter { it.length < MINIMUM_LENGTH_QUERY }
            .onEach { clearSearch() }
            .launchIn(viewModelScope)

        observeSearchInput(
            searchOperation = { gameRepository.searchGamesCombined(it) },
            onSearchSuccess = { results ->
                updateState {
                    it.copy(gameResults = results)
                }
            }
        )

        observeSearchInput(
            searchOperation = { songRepository.searchSongsCombined(it) },
            onSearchSuccess = { results ->
                updateState {
                    it.copy(songResults = results)
                }
            }
        )

        observeSearchInput(
            searchOperation = { composerRepository.searchComposersCombined(it) },
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
                songResults = emptyList(),
                gameResults = emptyList(),
                composerResults = emptyList(),
            )
        }
    }

    private fun <ModelType> observeSearchInput(
        searchOperation: suspend (String) -> Flow<List<ModelType>>,
        onSearchSuccess: suspend (List<ModelType>) -> Unit,
    ) {
        internalQueryFlow
            .debounce(DEBOUNCE_THRESHOLD)
            .filter { it.length >= MINIMUM_LENGTH_QUERY }
            .flatMapLatest(searchOperation)
            .catch { hatchet.e("Error searching: ${it.message}") }
            .onEach(onSearchSuccess)
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    companion object {
        internal const val DEBOUNCE_THRESHOLD = 300L
        private const val MINIMUM_LENGTH_QUERY = 3
    }
}
