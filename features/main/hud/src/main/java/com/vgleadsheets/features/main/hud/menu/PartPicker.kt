package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.perf.tracking.api.PerfTracker

@Suppress("LongParameterList")
object PartPicker {
    fun getListModels(
        expanded: Boolean,
        parts: List<PartSelectorItem>,
        onPartClick: (String) -> Unit,
        resources: Resources,
        perfTracker: PerfTracker
    ) = if (expanded) {
        parts.map {
            MenuItemListModel(
                resources.getString(it.longResId),
                null,
                R.drawable.ic_description_24dp,
                { onPartClick(it.apiId) },
                "",
                perfTracker
            )
        }
    } else {
        emptyList()
    }
}
