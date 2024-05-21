package com.vgleadsheets.topbar

import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.viewmodel.VglsViewModel
import kotlinx.coroutines.flow.update

class TopBarViewModel : VglsViewModel<TopBarState, TopBarEvent>() {
    override fun initialState() = TopBarState()

    fun updateTitle(title: TitleBarModel) {
        internalUiState.update {
            it.copy(model = title)
        }
    }

    fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.Menu -> {}
            is VglsAction.AppBack -> {}
        }
    }
}
