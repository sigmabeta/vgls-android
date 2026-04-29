package com.vgleadsheets.remaster.favorites

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
}
