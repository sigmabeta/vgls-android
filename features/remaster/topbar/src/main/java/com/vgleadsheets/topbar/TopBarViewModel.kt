package com.vgleadsheets.topbar

import com.vgleadsheets.viewmodel.VglsViewModel
import kotlinx.coroutines.flow.update

class TopBarViewModel(
    initialState: TopBarState = TopBarState()
) : VglsViewModel<TopBarState, TopBarEvent>(initialState) {
    fun updateTitle(title: String?) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateSubtitle(subtitle: String?) {
        _uiState.update {
            it.copy(subtitle = subtitle)
        }
    }
}
