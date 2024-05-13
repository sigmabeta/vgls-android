package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class ComposerClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
}
