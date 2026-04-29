package com.vgleadsheets.remaster.tags.list

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class TagKeyClicked(val id: Long) : Action()
}
