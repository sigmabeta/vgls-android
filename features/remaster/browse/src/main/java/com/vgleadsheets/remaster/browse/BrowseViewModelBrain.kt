package com.vgleadsheets.remaster.browse

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope

class BrowseViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> return
            is VglsAction.Resume -> return
            is Action.DestinationClicked -> onDestinationClicked(action.destination)
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onDestinationClicked(destination: String) {
        emitEvent(VglsEvent.NavigateTo(destination))
    }
}
