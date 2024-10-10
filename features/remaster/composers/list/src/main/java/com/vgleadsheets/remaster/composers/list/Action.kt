package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
}
