package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn

class HomeViewModelBrain(
    private val stringProvider: StringProvider,
    private val hatchet: Hatchet,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val homeModuleProvider: HomeModuleProvider,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> setup()
            is VglsAction.Resume -> return
            is VglsAction.NotifClearClicked -> onNotifClearClicked(action.id)
            is VglsAction.RefreshDbClicked -> onRefreshDbClicked()
            is Action.MostSongsGameClicked -> onMostSongsGameClicked(action.gameId)
            is Action.MostSongsComposerClicked -> onMostSongsComposerClicked(action.composerId)
            else -> onUnimplementedAction(action)
        }
    }

    private fun onMostSongsGameClicked(gameId: Long) {
        emitEvent(VglsEvent.NavigateTo(Destination.GAME_DETAIL.forId(gameId), Destination.HOME.destName))
    }

    private fun onMostSongsComposerClicked(composerId: Long) {
        emitEvent(VglsEvent.NavigateTo(Destination.COMPOSER_DETAIL.forId(composerId), Destination.HOME.destName))
    }

    private fun onUnimplementedAction(action: VglsAction) {
        emitEvent(
            VglsEvent.ShowSnackbar(
                message = "Unimplemented action: $action.",
                withDismissAction = false,
                actionDetails = null,
                source = Destination.HOME.destName
            )
        )
    }

    private fun onNotifClearClicked(id: Long) {
        emitEvent(VglsEvent.ClearNotif(id))
    }

    private fun onRefreshDbClicked() {
        emitEvent(VglsEvent.RefreshDb)
    }

    private fun setup() {
        val flows = homeModuleProvider.modules.map { it.moduleState }

        val combinedFlows = combine(flows) { moduleStates ->
            updateState { oldState ->
                (oldState as State).copy(
                    moduleStates = moduleStates.toList()
                )
            }
        }

        combinedFlows
            .flowOn(dispatchers.main)
            .launchIn(coroutineScope)
    }
}
