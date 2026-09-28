package com.vgleadsheets.remaster.tags.values

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class TagValueClicked(val id: Long) : Action()
}
