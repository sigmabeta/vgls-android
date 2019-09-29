package com.vgleadsheets.features.main.hud.parts

import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.model.parts.Part

data class PartSelectorItem(val apiId: String, val resId: Int, val selected: Boolean) {
    companion object {
        fun getDefaultPartPickerItems(selectedId: String? = null): List<PartSelectorItem> {
            val partsEnums = getDefaultPartsEnumList()

            val newSelectedId = selectedId ?: "C"
            return partsEnums
                .map { fromEnum(it) }
                .map { it.copy(selected = newSelectedId == it.apiId) }
        }

        fun getAvailablePartPickerItems(
            availableParts: List<Part>,
            selectedId: String?
        ): List<PartSelectorItem> {
            val partsEnums = getDefaultPartsEnumList()
            val availablePartIds = availableParts.map { it.name }

            val newSelection = if (selectedId != null && availablePartIds.contains(selectedId))
                selectedId
            else
                "C"

            return partsEnums
                .filter { availablePartIds.contains(it.apiId) }
                .map { fromEnum(it) }
                .map { it.copy(selected = newSelection == it.apiId) }
        }

        private fun getDefaultPartsEnumList(): ArrayList<PartSelectorOption> {
            return arrayListOf(
                PartSelectorOption.C,
                PartSelectorOption.B,
                PartSelectorOption.E,
                PartSelectorOption.F,
                PartSelectorOption.BASS,
                PartSelectorOption.ALTO,
                PartSelectorOption.VOCAL
            )
        }

        private fun fromEnum(enumValue: PartSelectorOption) = PartSelectorItem(
            enumValue.apiId,
            enumValue.resId,
            false
        )
    }
}
