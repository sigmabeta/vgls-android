package com.vgleadsheets.remaster.browse

import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.state.VglsAction

class BrowseViewModelBrain : ListViewModelBrain() {
    override fun initialState() = State

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> {}
            is Action.DestinationClicked -> onDestinationClicked(action.destination)
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onDestinationClicked(destination: String) {
        emitEvent(ListEvent.NavigateTo(destination))
    }
}
