package com.vgleadsheets.remaster.browse

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DestinationClicked(val destination: String) : Action()
}
