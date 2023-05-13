package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.PartSelectorOption

object TitleBar {
    fun getListModels(
        selectedPart: PartSelectorOption,
        hudMode: HudMode,
        resources: Resources,
        onSearchButtonClick: () -> Unit,
        onMenuClick: () -> Unit,
        onChangePartClick: () -> Unit,
    ): List<ListModel> = if (hudMode == HudMode.SEARCH) {
        emptyList()
    } else {
        listOf(
            MenuTitleBarListModel(
                resources.getString(selectedPart.longResId),
                getIconId(hudMode),
                onSearchButtonClick,
                onMenuClick,
                onChangePartClick,
            )
        )
    }

    private fun getIconId(hudMode: HudMode) = when (hudMode) {
        HudMode.REGULAR, HudMode.HIDDEN -> com.vgleadsheets.vectors.R.drawable.ic_menu_24dp
        HudMode.PERF -> com.vgleadsheets.vectors.R.drawable.ic_arrow_back_black_24dp
        else -> com.vgleadsheets.vectors.R.drawable.ic_clear_black_24dp
    }
}
