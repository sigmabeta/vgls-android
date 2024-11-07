package com.vgleadsheets.remaster.parts

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class PartSelected(val option: PartSelectorOption) : Action()
}
