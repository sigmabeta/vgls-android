package com.vgleadsheets.features.main.hud.parts

import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.model.ListItem
import com.vgleadsheets.model.parts.Part

data class PartSelectorItem(val apiId: String, val resId: Int, val selected: Boolean) :
    ListItem<PartSelectorItem> {
    override fun isTheSameAs(theOther: PartSelectorItem?) = apiId == theOther?.apiId

    override fun hasSameContentAs(theOther: PartSelectorItem?) = selected == theOther?.selected

    override fun getChangeType(theOther: PartSelectorItem?): Int {
        if (theOther == null) {
            return NO_CHANGE
        }

        return if (selected) {
            when {
                theOther.selected -> NO_CHANGE
                else -> GETTING_SELECTED
            }
        } else {
            when {
                theOther.selected -> GETTING_UNSELECTED
                else -> NO_CHANGE
            }
        }
    }

    companion object {
        const val GETTING_SELECTED = 1234
        const val GETTING_UNSELECTED = 5678
        const val NO_CHANGE = 2468

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
