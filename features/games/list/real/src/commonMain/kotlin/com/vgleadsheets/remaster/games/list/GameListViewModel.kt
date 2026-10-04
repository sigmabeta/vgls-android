package com.vgleadsheets.remaster.games.list

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Game
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlin.math.max
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.PaginationType
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * The browse games grid. Loads a name-ordered window one page at a time (SAGE's
 * [SageAction.LoadMoreRequested]/[SageAction.LoadPreviousRequested]) instead of the whole catalog, so
 * only the pages the user has scrolled to are in memory.
 */
@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class GameListViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val gameRepository: GameRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_GAME

    override fun initialState() = State()

    // One page request at a time; the active check coalesces the burst of scroll signals the grid
    // emits while a page is in flight.
    private var pageJob: Job? = null

    private val pageSize: Int
        get() = (internalUiState.value.paginationType as? PaginationType.Paginating)?.pageSize
            ?: PaginationType.DEFAULT_PAGE_SIZE

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> loadInitial()
            is Action.GameClicked -> onGameClicked(action.id)
            SageAction.LoadMoreRequested -> loadNextPage()
            SageAction.LoadPreviousRequested -> loadPreviousPage()
        }
    }

    /** (Re)open the window at [startOffset], discarding anything previously loaded. */
    private fun loadInitial(startOffset: Int = 0) {
        pageJob?.cancel()
        updateState {
            it.copy(
                games = LCE.Loading(LOAD_OPERATION_NAME),
                windowStart = startOffset,
                hasMoreBefore = startOffset > 0,
                hasMoreAfter = true,
                loadingPrevious = false,
                loadingMore = false,
            )
        }
        pageJob = scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            val page = gameRepository.getGamesPage(limit = pageSize, offset = startOffset)
            updateState { reduceInitial(it, page) }
        }
    }

    private fun loadNextPage() {
        if (pageJob?.isActive == true) return
        val current = internalUiState.value
        val loaded = (current.games as? LCE.Content)?.data ?: return
        if (!current.hasMoreAfter) return
        val offset = current.windowStart + loaded.size
        updateState { it.copy(loadingMore = true) }
        pageJob = scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            val page = gameRepository.getGamesPage(limit = pageSize, offset = offset)
            updateState { reduceAppend(it, page) }
        }
    }

    private fun loadPreviousPage() {
        if (pageJob?.isActive == true) return
        val current = internalUiState.value
        if (current.games !is LCE.Content || !current.hasMoreBefore) return
        val newStart = max(0, current.windowStart - pageSize)
        val count = current.windowStart - newStart
        if (count <= 0) return
        updateState { it.copy(loadingPrevious = true) }
        pageJob = scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            val page = gameRepository.getGamesPage(limit = count, offset = newStart)
            updateState { reducePrepend(it, page, newStart) }
        }
    }

    private fun reduceInitial(state: State, page: List<Game>) = state.copy(
        games = LCE.Content(page),
        loadingPrevious = false,
        loadingMore = false,
        hasMoreAfter = page.size >= pageSize,
    )

    private fun reduceAppend(state: State, page: List<Game>): State {
        val current = (state.games as? LCE.Content)?.data.orEmpty()
        return state.copy(
            games = LCE.Content(current + page),
            loadingMore = false,
            hasMoreAfter = page.size >= pageSize,
        )
    }

    private fun reducePrepend(state: State, page: List<Game>, newStart: Int): State {
        val current = (state.games as? LCE.Content)?.data.orEmpty()
        return state.copy(
            games = LCE.Content(page + current),
            windowStart = newStart,
            loadingPrevious = false,
            hasMoreBefore = newStart > 0,
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.GAMES_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "games.list"
    }
}
