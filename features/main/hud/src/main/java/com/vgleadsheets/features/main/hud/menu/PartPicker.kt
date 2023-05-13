package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.features.main.hud.PartSelectorOption
import com.vgleadsheets.model.Part

object PartPicker {
    fun getListModels(
        expanded: Boolean,
        showVocalOption: Boolean,
        onPartClick: (Part) -> Unit,
        resources: Resources,
        selectedPartId: String
    ): List<ListModel> = if (expanded) {
        generatePartPickerItems(showVocalOption)
            .map {
                MenuItemListModel(
                    resources.getString(it.longResId),
                    null,
                    com.vgleadsheets.vectors.R.drawable.ic_description_24dp,
                    { onPartClick(Part.valueOf(it.name)) },
                    it.apiId == selectedPartId
                )
            }
    } else {
        emptyList()
    }

    private fun generatePartPickerItems(showVocalsOption: Boolean) =
        PartSelectorOption
            .values()
            .filter { it.apiId != Part.VOCAL.apiId || showVocalsOption }
}
