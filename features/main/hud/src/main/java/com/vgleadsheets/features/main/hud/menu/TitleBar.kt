package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R

@Suppress("LongParameterList")
object TitleBar {
    fun getListModels(
        selectedPart: PartSelectorOption,
        hudMode: HudMode,
        resources: Resources,
        onMenuClick: () -> Unit,
        onChangePartClick: () -> Unit,
    ) = listOf(
        MenuTitleBarListModel(
            resources.getString(R.string.app_name),
            resources.getSelectedPartString(selectedPart),
            hudMode != HudMode.REGULAR,
            getIconId(hudMode),
            onMenuClick,
            onChangePartClick,
        )
    )

    private fun getIconId(hudMode: HudMode) = when (hudMode) {
        HudMode.REGULAR -> R.drawable.ic_menu_24dp
        HudMode.PERF -> R.drawable.ic_arrow_back_black_24dp
        else -> R.drawable.ic_clear_black_24dp
    }

    private fun Resources.getSelectedPartString(selectedPart: PartSelectorOption): String {
        return getString(R.string.subtitle_menu, getString(selectedPart.longResId))
    }
}
