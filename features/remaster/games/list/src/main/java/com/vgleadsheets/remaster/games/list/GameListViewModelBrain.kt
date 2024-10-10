package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Game
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class GameListViewModelBrain(
    private val gameRepository: GameRepository,
    private val scheduler: VglsScheduler,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> startLoading()
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
            VglsEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.GAMES_LIST.name
            )
        )
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            (it as State).copy(
                games = games
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "games.list"
    }
}
