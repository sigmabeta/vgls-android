package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.perf.tracking.api.PerfTracker

@Suppress("LongParameterList")
object PartPicker {
    fun getListModels(
        expanded: Boolean,
        showVocalOption: Boolean,
        onPartClick: (Part) -> Unit,
        resources: Resources,
        perfTracker: PerfTracker,
        selectedPartId: String
    ) = if (expanded) {
        generatePartPickerItems(showVocalOption)
            .map {
                MenuItemListModel(
                    resources.getString(it.longResId),
                    null,
                    R.drawable.ic_description_24dp,
                    it.apiId == selectedPartId,
                ) { onPartClick(Part.valueOf(it.name)) }
            }
    } else {
        emptyList()
    }

    private fun generatePartPickerItems(showVocalsOption: Boolean) =
        PartSelectorOption
            .values()
            .filter { it.apiId != Part.VOCAL.apiId || showVocalsOption }
}
