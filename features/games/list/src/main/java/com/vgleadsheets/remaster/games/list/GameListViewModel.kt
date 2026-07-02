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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

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

    init {
        sendAction(SageAction.InitNoArgs)
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
            is Action.GameClicked -> onGameClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectGames()
    }

    private fun collectGames() {
        gameRepository.getAllGames()
            .onEach(::onGamesLoaded)
            .catch { error -> showError(LOAD_OPERATION_NAME, error) }
            .runInBackground()
    }

    private fun onGamesLoaded(games: List<Game>) {
        updateGames(LCE.Content(games))
    }

    private fun showLoading() {
        updateGames(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(loadOperationName: String, error: Throwable) {
        updateGames(LCE.Error(loadOperationName, error))
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.GAMES_LIST.name
            )
        )
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            it.copy(
                games = games
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "games.list"
    }
}
