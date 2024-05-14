package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class GameClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
}
