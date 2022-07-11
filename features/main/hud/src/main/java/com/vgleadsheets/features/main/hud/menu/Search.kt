package com.vgleadsheets.features.main.hud.menu

import com.vgleadsheets.components.MenuSearchListModel
import com.vgleadsheets.features.main.hud.HudMode

object Search {
    fun getListModels(
        hudMode: HudMode,
        searchQuery: String?,
        onTextEntered: (String) -> Unit,
        onMenuButtonClick: () -> Unit,
        onClearClick: () -> Unit
    ) = if (hudMode == HudMode.SEARCH) {
        listOf(
            MenuSearchListModel(
                searchQuery,
                onTextEntered,
                onMenuButtonClick,
                onClearClick
            )
        )
    } else {
        emptyList()
    }
}
