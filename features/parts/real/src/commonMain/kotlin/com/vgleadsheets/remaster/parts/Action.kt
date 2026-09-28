package com.vgleadsheets.remaster.parts

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class PartSelected(val option: PartSelectorOption) : Action()
}
