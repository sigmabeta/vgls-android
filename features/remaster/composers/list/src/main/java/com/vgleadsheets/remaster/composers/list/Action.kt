package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
}
