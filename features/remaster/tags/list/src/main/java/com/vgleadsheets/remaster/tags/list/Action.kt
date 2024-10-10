package com.vgleadsheets.remaster.tags.list

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class TagKeyClicked(val id: Long) : Action()
}
