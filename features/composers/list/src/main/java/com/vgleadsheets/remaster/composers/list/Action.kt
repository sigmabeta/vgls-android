package com.vgleadsheets.remaster.composers.list

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
}
