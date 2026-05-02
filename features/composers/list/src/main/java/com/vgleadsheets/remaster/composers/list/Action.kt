package com.vgleadsheets.remaster.composers.list

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class ComposerClicked(val id: Long) : Action()
}
