package com.vgleadsheets.remaster.tags.values

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class TagValueClicked(val id: Long) : Action()
}
