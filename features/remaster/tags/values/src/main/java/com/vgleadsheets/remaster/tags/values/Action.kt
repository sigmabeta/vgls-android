package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class TagValueClicked(val id: Long) : Action()
}
