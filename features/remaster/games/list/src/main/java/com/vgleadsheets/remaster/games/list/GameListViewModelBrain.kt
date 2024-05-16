package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.model.Game
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GameListViewModelBrain(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    stringProvider: StringProvider,
    ) : ListViewModelBrain(stringProvider) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> collectGames()
            is Action.GameClicked -> onGameClicked(action.id)
        }
    }

    private fun collectGames() {
        repository.getAllGames()
            .onEach(::onGamesLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onGamesLoaded(games: List<Game>) {
        updateState {
            (it as State).copy(
                games = games
            )
        }
    }

    private fun onGameClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.GAME_DETAIL.forId(id)))
    }
}
