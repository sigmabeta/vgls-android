package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R

object TitleBar {
    fun getListModels(
        selectedPart: PartSelectorOption,
        hudMode: HudMode,
        resources: Resources,
        onSearchButtonClick: () -> Unit,
        onMenuClick: () -> Unit,
        onChangePartClick: () -> Unit,
    ) = if (hudMode == HudMode.SEARCH) {
        emptyList()
    } else {
        listOf(
            MenuTitleBarListModel(
                resources.getString(selectedPart.midLengthResId),
                getIconId(hudMode),
                onSearchButtonClick,
                onMenuClick,
                onChangePartClick,
            )
        )
    }

    private fun getIconId(hudMode: HudMode) = when (hudMode) {
        HudMode.REGULAR, HudMode.HIDDEN -> R.drawable.ic_menu_24dp
        HudMode.PERF -> R.drawable.ic_arrow_back_black_24dp
        else -> R.drawable.ic_clear_black_24dp
    }
}
