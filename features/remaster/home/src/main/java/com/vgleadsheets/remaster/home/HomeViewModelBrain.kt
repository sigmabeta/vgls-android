package com.vgleadsheets.remaster.home

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.CoroutineScope

class HomeViewModelBrain(
    stringProvider: StringProvider,
    hatchet: Hatchet,
    dispatchers: VglsDispatchers,
    coroutineScope: CoroutineScope
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

            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }
}
