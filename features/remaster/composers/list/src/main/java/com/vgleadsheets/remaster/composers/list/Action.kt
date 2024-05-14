package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class ComposerClicked(val id: Long) : Action()
}
