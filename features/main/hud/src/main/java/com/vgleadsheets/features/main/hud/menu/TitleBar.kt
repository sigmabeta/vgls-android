package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.features.main.hud.R

@Suppress("LongParameterList")
object TitleBar {
    fun getListModels(
        selectedPart: PartSelectorOption,
        expanded: Boolean,
        resources: Resources,
        onMenuClick: () -> Unit,
        onChangePartClick: () -> Unit,
    ) = listOf(
        MenuTitleBarListModel(
            resources.getString(R.string.app_name),
            resources.getSelectedPartString(selectedPart),
            expanded,
            onMenuClick,
            onChangePartClick,
        )
    )

    private fun Resources.getSelectedPartString(selectedPart: PartSelectorOption): String {
        return getString(R.string.subtitle_menu, getString(selectedPart.longResId))
    }
}
