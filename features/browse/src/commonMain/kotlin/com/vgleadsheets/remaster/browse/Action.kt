package com.vgleadsheets.remaster.browse

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class DestinationClicked(val destination: String) : Action()
}
