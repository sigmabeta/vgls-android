package com.vgleadsheets.remaster.tags.list

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class TagKeyClicked(val id: Long) : Action()
}
