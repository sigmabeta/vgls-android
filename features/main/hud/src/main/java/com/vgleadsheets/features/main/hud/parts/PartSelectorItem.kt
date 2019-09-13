package com.vgleadsheets.features.main.hud.parts

import com.vgleadsheets.common.parts.PartSelectorOption
import com.vgleadsheets.model.ListItem

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

        fun fromEnum(enumValue: PartSelectorOption) = PartSelectorItem(
            enumValue.apiId,
            enumValue.resId,
            false
        )
    }
}