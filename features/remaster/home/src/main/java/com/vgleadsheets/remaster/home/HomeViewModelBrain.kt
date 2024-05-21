package com.vgleadsheets.remaster.home

import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider

class HomeViewModelBrain(stringProvider: StringProvider) : ListViewModelBrain(stringProvider) {
    override fun initialState() = State

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> return
            is VglsAction.Resume -> return

            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }
}
