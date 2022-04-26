package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuTitleBarListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.perf.tracking.api.PerfTracker

@Suppress("LongParameterList")
object TitleBar {
    fun getListModels(
        selectedPart: PartSelectorItem,
        expanded: Boolean,
        resources: Resources,
        onMenuClick: () -> Unit,
        onChangePartClick: () -> Unit,
        perfTracker: PerfTracker
    ) = listOf(
        MenuTitleBarListModel(
            resources.getString(R.string.app_name),
            resources.getSelectedPartString(selectedPart),
            expanded,
            onMenuClick,
            onChangePartClick,
            "",
            perfTracker
        )
    )

    private fun Resources.getSelectedPartString(selectedPart: PartSelectorItem): String {
        return getString(R.string.subtitle_menu, getString(selectedPart.longResId))
    }
}
