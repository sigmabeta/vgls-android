package com.vgleadsheets.topbar

import com.vgleadsheets.viewmodel.VglsViewModel
import kotlinx.coroutines.flow.update

class TopBarViewModel : VglsViewModel<TopBarState, TopBarEvent>() {
    override fun initialState() = TopBarState()

    fun updateTitle(title: String?) {
        internalUiState.update {
            it.copy(title = title)
        }
    }

    fun updateSubtitle(subtitle: String?) {
        internalUiState.update {
            it.copy(subtitle = subtitle)
        }
    }
}
