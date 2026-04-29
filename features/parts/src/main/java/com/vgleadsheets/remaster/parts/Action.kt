package com.vgleadsheets.remaster.parts

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class PartSelected(val option: PartSelectorOption) : Action()
}
