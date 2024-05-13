package com.vgleadsheets.remaster.browse

import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain

class BrowseViewModelBrain : ListViewModelBrain() {
    override fun initialState() = State

    override fun handleAction(action: ListAction) {
        when (action) {
            is ListAction.InitNoArgs -> {}
            is Action.DestinationClicked -> onDestinationClicked(action.destination)
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onDestinationClicked(destination: String) {
        emitEvent(ListEvent.NavigateTo(destination))
    }
}
